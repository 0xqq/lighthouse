package com.huya.lighthouse.server.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.type.Status;
import com.huya.lighthouse.server.TaskAction;
import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.server.factory.InstanceTaskCacheFactory;
import com.huya.lighthouse.server.factory.InstanceTaskLockFactory;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.model.InstanceTaskKey;
import com.huya.lighthouse.server.model.InstanceTaskLock;
import com.huya.lighthouse.util.SSHAsynExecUtils;

public class AgentSelector {

	protected static Logger logger = LoggerFactory.getLogger(AgentSelector.class);

	public static DefAgentGroup select(InstanceTaskExecutor instanceTaskExecutor) throws Exception {
		DefAgentGroup targetDefAgentGroup = null;
		InstanceTask instanceTask = instanceTaskExecutor.getInstanceTask();
		// 重启恢复现场后，可能有些任务之前已经在运行中了
		if (StringUtils.isNotBlank(instanceTask.getAgentHostRun())) {
			return DefAgentGroupFactory.getAgentHost(instanceTask.getAgentHostGroup(), instanceTask.getAgentHostRun());
		}
		Map<String, DefAgentGroup> agentHostMap = DefAgentGroupFactory.getByAgentHostGroup(instanceTask.getAgentHostGroup());
		if (agentHostMap == null || agentHostMap.size() == 0) {
			throw new Exception("No agent to exec: " + instanceTask);
		}

		InstanceTask preInstanceTask = ServiceBeanFactory.getInstanceTaskService().getSameAgentRun(instanceTask.getTaskId(), instanceTask.getTaskDate());
		if (preInstanceTask != null) {
			targetDefAgentGroup = DefAgentGroupFactory.getAgentHost(preInstanceTask.getAgentHostGroup(), preInstanceTask.getAgentHostRun());
			boolean isFreeOK = isMemoryOK(targetDefAgentGroup);
			boolean isDfOK = isDiskOK(targetDefAgentGroup);
			int n = 0;
			while (!(isFreeOK && isDfOK)) {
				logger.info("SameAgentRun wait satisfy: " + instanceTaskExecutor.getInstanceTaskKeyDetail());
				if (InstanceTaskExecutor.isDone(instanceTask)) {
					return null;
				}
				if (n == 20) {
					logger.warn("SameAgentRun wait satisfy fail: " + instanceTaskExecutor.getInstanceTaskKeyDetail());
					agentHostMap = DefAgentGroupFactory.getByAgentHostGroup(instanceTask.getAgentHostGroup());
					if (agentHostMap == null || agentHostMap.size() <= 1) {
						return null;
					}

					List<InstanceTask> sameAgentInstanceList = new ArrayList<InstanceTask>();
					instanceTask.setStatus(Status.INIT.name());
					sameAgentInstanceList.add(instanceTask);
					InstanceTask minPreInstanceTask = null;
					while (preInstanceTask != null) {
						preInstanceTask.setStatus(Status.INIT.name());
						sameAgentInstanceList.add(preInstanceTask);
						minPreInstanceTask = preInstanceTask;
						preInstanceTask = ServiceBeanFactory.getInstanceTaskService().getSameAgentRun(preInstanceTask.getTaskId(), preInstanceTask.getTaskDate());
					}
					for (InstanceTask sameAgentInstance : sameAgentInstanceList) {
						InstanceTaskKey instanceTaskKey = new InstanceTaskKey(sameAgentInstance.getTaskId(), sameAgentInstance.getTaskDate());
						InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
						instanceTaskLock.getLock().lock();
						try {
							ServiceBeanFactory.getInstanceTaskService().update(minPreInstanceTask);
							InstanceTaskCacheFactory.put(sameAgentInstance);
						} finally {
							InstanceTaskLockFactory.returnLock(instanceTaskLock);
						}
					}
					TaskAction.run(minPreInstanceTask.getTaskId(), minPreInstanceTask.getTaskDate(), minPreInstanceTask.getInstanceId(), 0, minPreInstanceTask.getIsForceRun());
					return null;
				}
				Thread.sleep(120000l);
				isFreeOK = isMemoryOK(targetDefAgentGroup);
				isDfOK = isDiskOK(targetDefAgentGroup);
				n++;
				instanceTask = instanceTaskExecutor.getInstanceTask();
			}
			if (targetDefAgentGroup != null) {
				return targetDefAgentGroup;
			}
		}

		TargetAgent targetAgent = new TargetAgent(targetDefAgentGroup, 0.0, 0.0);
		List<DefAgentGroup> tmpList = new ArrayList<DefAgentGroup>(agentHostMap.values());
		Collections.shuffle(tmpList);
		
		int i = 0;
		while (i <= 720) {
			List<DefAgentGroup> agentHostHis = new ArrayList<DefAgentGroup>();
			for (DefAgentGroup defAgentGroup : tmpList) {
				if (StringUtils.contains(instanceTask.getAgentHostRunHis(), defAgentGroup.getAgentHost())) {
					agentHostHis.add(defAgentGroup);
					continue;
				}
				select(defAgentGroup, targetAgent);
				if (targetAgent.isOK()) {
					return defAgentGroup;
				}
			}
			if (targetAgent.getDefAgentGroup() != null) {
				return targetAgent.getDefAgentGroup();
			}
	
			for (DefAgentGroup defAgentGroup : agentHostHis) {
				select(defAgentGroup, targetAgent);
				if (targetAgent.isOK()) {
					return defAgentGroup;
				}
			}
			if (targetAgent.getDefAgentGroup() != null) {
				return targetAgent.getDefAgentGroup();
			}
			logger.error("Retry " + i + ", No agent to exec: " + instanceTask);
			Thread.sleep(120000l);
			i++;
		}
		return null;
	}

	private static void select(DefAgentGroup defAgentGroup, TargetAgent targetAgent) {
		DefAgentGroup targetDefAgentGroup = targetAgent.getDefAgentGroup();
		double curMemUseRate= getMemoryUseRate(defAgentGroup);
		boolean dfFlag = isDiskOK(defAgentGroup);
		if (curMemUseRate<=0.9 && dfFlag) {
			double curLoadVal = getLoadVal(defAgentGroup);
			if ((targetDefAgentGroup == null) || (curLoadVal > 1 && curLoadVal < targetAgent.getLoadVal()) || (curLoadVal <= 1 && curMemUseRate < targetAgent.getMemUseRate())) {
				targetAgent.setDefAgentGroup(defAgentGroup);
				targetAgent.setLoadVal(curLoadVal);
				targetAgent.setMemUseRate(curMemUseRate);
			}
		}
	}

	public static double getLoadVal(DefAgentGroup defAgentGroup) {
		return SSHAsynExecUtils.getLoadRate(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword());
	}

	public static boolean isDiskOK(DefAgentGroup defAgentGroup) {
		return SSHAsynExecUtils.isDiskOK(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword());
	}

	public static boolean isMemoryOK(DefAgentGroup defAgentGroup) {
		double useRate = getMemoryUseRate(defAgentGroup);
		if (useRate > 0.9) {
			return false;
		}
		return true;
	}
	
	public static double getMemoryUseRate(DefAgentGroup defAgentGroup) {
		return SSHAsynExecUtils.getMemoryUseRate(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword());
	}

	static class TargetAgent {
		private DefAgentGroup defAgentGroup;
		private double loadVal;
		private double memUseRate;

		public TargetAgent(DefAgentGroup defAgentGroup, double loadVal, double memUseRate) {
			super();
			this.defAgentGroup = defAgentGroup;
			this.loadVal = loadVal;
			this.memUseRate = memUseRate;
		}

		public DefAgentGroup getDefAgentGroup() {
			return defAgentGroup;
		}

		public void setDefAgentGroup(DefAgentGroup defAgentGroup) {
			this.defAgentGroup = defAgentGroup;
		}

		public double getLoadVal() {
			return loadVal;
		}

		public void setLoadVal(double loadVal) {
			this.loadVal = loadVal;
		}

		public double getMemUseRate() {
			return memUseRate;
		}

		public void setMemUseRate(double memUseRate) {
			this.memUseRate = memUseRate;
		}

		public boolean isOK() {
			return defAgentGroup != null && loadVal <= 1 && memUseRate<=0.75;
		}
	}
}
