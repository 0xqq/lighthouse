package com.huya.lighthouse.server.executor;

import com.huya.lighthouse.model.bo.misc.InstanceStatus;
import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.po.def.DefMonitorDur;
import com.huya.lighthouse.model.po.def.DefMonitorRetry;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;
import com.huya.lighthouse.model.po.instance.InstanceTaskLog;
import com.huya.lighthouse.model.type.Status;
import com.huya.lighthouse.server.TaskAction;
import com.huya.lighthouse.server.factory.*;
import com.huya.lighthouse.server.instance.initiator.InstanceTaskDependInitiator;
import com.huya.lighthouse.server.model.InstanceTaskKey;
import com.huya.lighthouse.server.model.InstanceTaskKeyDetail;
import com.huya.lighthouse.server.model.InstanceTaskLock;
import com.huya.lighthouse.server.util.AlertUtils;
import com.huya.lighthouse.server.util.TriggerExtServiceUtils;
import com.huya.lighthouse.util.DateUtils2;
import com.huya.lighthouse.util.SSHAsynExecUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 任务实例执行器
 */
public class InstanceTaskExecutor implements Comparable<InstanceTaskExecutor>, Callable<String>, Serializable {

	private static Logger logger = LoggerFactory.getLogger(InstanceTaskExecutor.class);

	private InstanceTaskKeyDetail instanceTaskKeyDetail;
	private DefAgentGroup defAgentGroup;
	private String instanceDir;
	private String context = null;

	public InstanceTaskExecutor(InstanceTaskKeyDetail instanceTaskKeyDetail) throws Exception {
		super();
		if (instanceTaskKeyDetail == null || instanceTaskKeyDetail.getTaskId() == null || instanceTaskKeyDetail.getTaskDate() == null || instanceTaskKeyDetail.getInstanceId() == null) {
			throw new Exception("InstanceTaskKeyDetail cannot be null");
		}
		this.instanceTaskKeyDetail = instanceTaskKeyDetail;
	}

	public InstanceTaskKeyDetail getInstanceTaskKeyDetail() {
		return instanceTaskKeyDetail;
	}

	@Override
	public String call() {
		try {
			Thread.currentThread().setName("call: " + instanceTaskKeyDetail);
			String status = null;
			logger.info("Start call: {}", instanceTaskKeyDetail);
			InstanceTaskKey instanceTaskKey = instanceTaskKeyDetail.getInstanceTaskKey();
			InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
			instanceTaskLock.getLock().lock();
			try {
				InstanceTask instanceTask = getInstanceTask();
				if (isInvalidInstanceTask(instanceTask)) {
					return "";
				}
				// 考虑重启,kill
				if (isDone(instanceTask)) {
					return instanceTask.getStatus();
				}
				setStartStatus(instanceTask);
			} catch (Exception e) {
				logger.error("call start: " + instanceTaskKeyDetail, e);
				insertInstanceTaskLog(e);
				return "";
			} finally {
				InstanceTaskLockFactory.returnLock(instanceTaskLock);
			}

			try {
				status = exec();
			} catch (Exception e) {
				logger.error("exec: " + instanceTaskKeyDetail, e);
				status = Status.FAIL.name();
				insertInstanceTaskLog(e);
			}

			instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
			instanceTaskLock.getLock().lock();
			try {
				InstanceTask instanceTask = getInstanceTask();
				if (isInvalidInstanceTask(instanceTask)) {
					return "";
				}
				if (!StringUtils.equals(instanceTask.getStatus(), Status.RUNNING.name())) {
					return instanceTask.getStatus();
				}
				if (StringUtils.isNotBlank(this.context)) {
					instanceTask.setContext(this.context);
				}
				setEndStatus(instanceTask, status, 1);
			} finally {
				InstanceTaskLockFactory.returnLock(instanceTaskLock);
				rmSubmittedPath();
            }

            return status;
        } catch (Throwable e) {
            logger.error("call: " + instanceTaskKeyDetail, e);
            insertInstanceTaskLog(e);
            return "";
        } finally {
		    try {
                TaskRunningFactory.delInstatnceTask(instanceTaskKeyDetail);
            } catch (Exception e) {
                logger.error("delInstatnceTask" + instanceTaskKeyDetail, e);
            }

            try {
				triggerExtService(getInstanceTask());
			} catch (Exception e) {
				logger.error("triggerExtService task: " + instanceTaskKeyDetail, e);
			}
			
			try {
				doRetry();
			} catch (Exception e) {
				logger.error("doRetry task: " + instanceTaskKeyDetail, e);
			}
			
			try {
				doNext(getInstanceTask());
			} catch (Exception e) {
				logger.error("doNext task: " + instanceTaskKeyDetail, e);
			}
			
			try {
				InstanceTask instanceTask = getInstanceTask();
				if (!isInvalidInstanceTask(instanceTask) && instanceTask.getIsOneTimes() == 1 && isDone(instanceTask)) {
					ServiceBeanFactory.getbODefTaskService().offlineByIdServer(instanceTask.getTaskId());
				}
				Thread.currentThread().setName("end: " + instanceTaskKeyDetail);
			} catch (Exception e) {
				logger.error("Invalid one times task: " + instanceTaskKeyDetail, e);
			}
		}
	}
	
	private void triggerExtService(InstanceTask instanceTask) throws Exception {
		if (isInvalidInstanceTask(instanceTask)) {
			return;
		}
		TriggerExtServiceUtils.trigger(instanceTask.getCatalogId(), instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getStatus(), instanceTask.getMaxRetryNum(), instanceTask.getRetriedNum());
	}

	private static boolean isInvalidInstanceTask(InstanceTask instanceTask) {
		return instanceTask == null || instanceTask.getIsValid() == null || instanceTask.getIsValid() == 0;
	}

	private void insertInstanceTaskLog(Throwable e) {
		InstanceTask instanceTask = getInstanceTask();
		InstanceTaskLog instanceTaskLog = new InstanceTaskLog(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId(), System.currentTimeMillis());
		instanceTaskLog.setContent(ExceptionUtils.getFullStackTrace(e));
		ServiceBeanFactory.getInstanceTaskLogService().create(instanceTaskLog);
	}

	public InstanceTask getInstanceTask() {
		InstanceTaskKey instanceTaskKey = instanceTaskKeyDetail.getInstanceTaskKey();
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		try {
			return getInstanceNoLock();
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}
	}

	private InstanceTask getInstanceNoLock() {
		return InstanceTaskCacheFactory.get(instanceTaskKeyDetail);
	}

	public static void doNext(final InstanceTask instanceTask) {
		if (instanceTask.getIsSelfRun() == 1) {
			return;
		}
		if (isCanNext(instanceTask)) {
			List<InstanceTaskDepend> instanceTaskPostDependList = InstanceTaskDependInitiator.initPost(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
			if (instanceTaskPostDependList == null) {
				return;
			}
			for (final InstanceTaskDepend instanceTaskDepend : instanceTaskPostDependList) {
				if (instanceTaskDepend == null) {
					continue;
				}
				//跨天场景，防止重跑很久之前的日期，引发爆炸性调用，涉及自依赖，滑动依赖
				if (!DateUtils.isSameDay(instanceTaskDepend.getTaskDate(), instanceTaskDepend.getPreTaskDate())) {
					InstanceTaskKey instanceTaskKey = new InstanceTaskKey(instanceTaskDepend.getTaskId(), instanceTaskDepend.getTaskDate());
					InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
					instanceTaskLock.getLock().lock();
					try {
						List<InstanceStatus> instanceStatusList = ServiceBeanFactory.getInstanceTaskService().getInstanceStatuss(instanceTaskDepend.getTaskId(), instanceTaskDepend.getTaskDate(), instanceTaskDepend.getTaskDate());
						if ( !(instanceStatusList != null && instanceStatusList.size() == 1 && StringUtils.equals(instanceStatusList.get(0).getStatus(), Status.INIT.name())) ) {
							continue;
						}
					} catch (Exception e) {
						logger.error("", e);
					} finally {
						InstanceTaskLockFactory.returnLock(instanceTaskLock);
					}
				}
				ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.currentThread().setName("submit next to: " + instanceTaskDepend.getTaskId() + ", " + instanceTaskDepend.getTaskDate());
							TaskAction.run(instanceTaskDepend.getTaskId(), instanceTaskDepend.getTaskDate(), instanceTask.getInstanceId(), 0, 0);
						} catch (Exception e) {
							logger.error("submit next to: " + instanceTaskDepend.getTaskId() + ", " + instanceTaskDepend.getTaskDate(), e);
						}
					}
				});
			}
		}
	}

	private void doRetry() {
		if (!LeaderSelector.isLeader) {
			return;
		}

		final InstanceTask instanceTask = getInstanceTask();
        if (isInvalidInstanceTask(instanceTask)) {
            return;
        }
		if (StringUtils.equals(instanceTask.getStatus(), Status.FAIL.name())) {
			List<DefMonitorRetry> defMonitorRetryList = ServiceBeanFactory.getDefMonitorRetryService().getByTaskId(instanceTaskKeyDetail.getTaskId(), 1);
			if (defMonitorRetryList != null) {
				for (DefMonitorRetry defMonitorRetry : defMonitorRetryList) {
					if (defMonitorRetry.getFailRetryN() == instanceTask.getRetriedNum()) {
						AlertUtils.alertMonitorRetry(instanceTask.getCatalogId(), instanceTask.getTaskId(), instanceTask.getTaskName(), instanceTask.getTaskDate(), instanceTask.getRetriedNum());
						break;
					}
				}
			}
		}

		if (StringUtils.equals(instanceTask.getStatus(), Status.FAIL.name()) && instanceTask.getRetriedNum() < instanceTask.getMaxRetryNum()) {
			final InstanceTaskExecutor curExecutor = this;
			ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.currentThread().setName("doRetry: " + instanceTaskKeyDetail);
						if (instanceTask.getRetryInterval() > 0) {
							Thread.sleep(instanceTask.getRetryInterval() * 1000l);
						}

						InstanceTaskKey instanceTaskKey = instanceTaskKeyDetail.getInstanceTaskKey();
						InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
						instanceTaskLock.getLock().lock();
						try {
							InstanceTask curInstanceTask = getInstanceTask();
							if (isInvalidInstanceTask(curInstanceTask)) {
								return;
							}
							if (StringUtils.equals(curInstanceTask.getStatus(), Status.FAIL.name()) && curInstanceTask.getRetriedNum() < curInstanceTask.getMaxRetryNum()) {
								retriedNumAdd1(curInstanceTask);
								TaskRunningFactory.submit(curExecutor);
							}
						} finally {
							InstanceTaskLockFactory.returnLock(instanceTaskLock);
						}
					} catch (Exception e) {
						logger.error("doRetry: " + instanceTaskKeyDetail, e);
					}
				}
			});
		}
	}

	private String exec() throws Exception {
		InstanceTask instanceTask = getInstanceTask();
		if (instanceTask.isVirtualTask()) {
			return Status.SUCCESS.name();
		}

		DefAgentGroup targetDefAgentGroup = AgentSelector.select(this);
		if (targetDefAgentGroup == null) {
			throw new Exception("No agent to exec: " + instanceTaskKeyDetail);
		}
		InstanceTaskKey instanceTaskKey = instanceTaskKeyDetail.getInstanceTaskKey();
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		try {
			instanceTask = getInstanceTask();
			if (isInvalidInstanceTask(instanceTask) || !isRunning(instanceTask)) {
				return instanceTask.getStatus();
			}
			instanceTask.setAgentHostRun(targetDefAgentGroup.getAgentHost());
			if (StringUtils.isNotBlank(instanceTask.getAgentHostRunHis())) {
				if (!StringUtils.contains(instanceTask.getAgentHostRunHis(), targetDefAgentGroup.getAgentHost())) {
					instanceTask.setAgentHostRunHis(instanceTask.getAgentHostRunHis() + "," + targetDefAgentGroup.getAgentHost());
				}
			} else {
				instanceTask.setAgentHostRunHis(targetDefAgentGroup.getAgentHost());
			}
			ServiceBeanFactory.getInstanceTaskService().update(instanceTask);
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}

		return exec(targetDefAgentGroup);
	}

	private String exec(DefAgentGroup defAgentGroup) throws Exception {
		String agentHost = defAgentGroup.getAgentHost();
		Integer agentPort = defAgentGroup.getAgentPort();
		String agentUser = defAgentGroup.getAgentUser();
		String agentPrivateKey = defAgentGroup.getAgentPrivateKey();
		String agentPassword = defAgentGroup.getAgentPassword();

		String instanceDir = mkdir(defAgentGroup);
		this.defAgentGroup = defAgentGroup;
		this.instanceDir = instanceDir;
		InstanceTask instanceTask = getInstanceTask();

		boolean isSubmitted = SSHAsynExecUtils.isSubmitted(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);

		if (!isSubmitted) {
			long logId = System.currentTimeMillis();
			InstanceTaskLog instanceTaskLog = new InstanceTaskLog(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId(), logId);
			instanceTaskLog.setAgentHostRun(defAgentGroup.getAgentHost());
			instanceTaskLog.setLogPath(SSHAsynExecUtils.getLogPath(instanceDir, logId));
			ServiceBeanFactory.getInstanceTaskLogService().create(instanceTaskLog);

			sendScript(instanceTask, instanceDir, logId, defAgentGroup);
			SSHAsynExecUtils.submitAsynSh(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);
		}

		List<DefMonitorDur> defMonitorDurList = ServiceBeanFactory.getDefMonitorDurService().getByTaskId(instanceTaskKeyDetail.getTaskId(), 1);
		boolean isRunning = SSHAsynExecUtils.isRunning(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);
		while (isRunning) {
			Thread.sleep(5000l);
			logger.info("Running: " + instanceTaskKeyDetail);
			InstanceTaskKey instanceTaskKey = instanceTaskKeyDetail.getInstanceTaskKey();
			InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
			instanceTaskLock.getLock().lock();
			try {
				instanceTask = getInstanceTask();
				if (isInvalidInstanceTask(instanceTask) || !isRunning(instanceTask)) {
					return instanceTask.getStatus();
				}
				if (System.currentTimeMillis() - instanceTask.getStartTime().getTime() > instanceTask.getMaxRunSec() * 1000l) {
					kill(instanceTask);
					return Status.FAIL.name();
				}
			} finally {
				InstanceTaskLockFactory.returnLock(instanceTaskLock);
			}
			isRunning = SSHAsynExecUtils.isRunning(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);

			if (defMonitorDurList != null && defMonitorDurList.size() > 0) {
				long dur = (System.currentTimeMillis() - instanceTask.getStartTime().getTime()) / 1000;
				int i = 0;
				for (DefMonitorDur defMonitorDur : defMonitorDurList) {
					if (defMonitorDur.getDurSec() <= dur) {
						AlertUtils.alertMonitorDur(instanceTask.getCatalogId(), instanceTask.getTaskId(), instanceTask.getTaskName(), instanceTask.getTaskDate(), defMonitorDur.getDurSec(), dur);
						defMonitorDurList.remove(i);
						break;
					}
					i++;
				}
			}
		}
		boolean isSuccess = SSHAsynExecUtils.isSuccess(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);
		this.context = SSHAsynExecUtils.getContextResult(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);
		if (isSuccess) {
			return Status.SUCCESS.name();
		}
		return Status.FAIL.name();
	}

	private void rmSubmittedPath() {
		if (defAgentGroup != null) {
			SSHAsynExecUtils.rmSubmittedPath(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(),
					defAgentGroup.getAgentPassword(), instanceDir);
		}
	}

	public static String getInstanceDir(InstanceTask instanceTask, String workBaseDir) {
		String yyyyMMdd = DateFormatUtils.format(instanceTask.getTaskDate(), "yyyyMMdd");
		String yyyyMMddHHmmss = DateFormatUtils.format(instanceTask.getTaskDate(), "yyyyMMddHHmmss");
		StringBuilder instanceDirBuilder = new StringBuilder();
		instanceDirBuilder.append(getInstanceBaseDir(workBaseDir)).append("/").append(yyyyMMdd).append("/").append(instanceTask.getCatalogId()).append("/").append(instanceTask.getTaskId()).append("/")
				.append(yyyyMMddHHmmss).append("/").append(instanceTask.getInstanceId());
		String instanceDir = instanceDirBuilder.toString();
		return instanceDir;
	}

	private String mkdir(DefAgentGroup defAgentGroup) {
		InstanceTask instanceTask = getInstanceTask();
		String instanceDir = getInstanceDir(instanceTask, defAgentGroup.getWorkBaseDir());
		SSHAsynExecUtils.mkdir(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword(),
				instanceDir);
		return instanceDir;
	}

	private void sendScript(InstanceTask instanceTask, String instanceDir, long logId, DefAgentGroup defAgentGroup) throws Exception {
		String agentHost = defAgentGroup.getAgentHost();
		Integer agentPort = defAgentGroup.getAgentPort();
		String agentUser = defAgentGroup.getAgentUser();
		String agentPrivateKey = defAgentGroup.getAgentPrivateKey();
		String agentPassword = defAgentGroup.getAgentPassword();

		List<String> preContexts = ServiceBeanFactory.getInstanceTaskService().getPreContexts(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("taskId", instanceTask.getTaskId());
		paramMap.put("taskDate", instanceTask.getTaskDate());
		paramMap.put("catalogId", instanceTask.getCatalogId());
		paramMap.put("taskBody", instanceTask.getTaskBody());
		if (preContexts != null && preContexts.size() > 0) {
			paramMap.put("preContexts", preContexts);
		}
		SSHAsynExecUtils.sendParamMap(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir, paramMap);

		String pluginPath = getTaskPluginDir(defAgentGroup.getWorkBaseDir());
		String program = StringUtils.replace(instanceTask.getTaskPlugin(), "$pluginDir", pluginPath);
		
		SSHAsynExecUtils.sendSynSh(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir, program, instanceTask.getLinuxRunUser());
		SSHAsynExecUtils.sendAsynSh(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir, logId);
		SSHAsynExecUtils.sendIsRunningSh(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);
		SSHAsynExecUtils.sendKillSh(agentHost, agentPort, agentUser, agentPrivateKey, agentPassword, instanceDir);
	}

	public static boolean kill(InstanceTask instanceTask) throws Exception {
		DefAgentGroup defAgentGroup = DefAgentGroupFactory.getAgentHost(instanceTask.getAgentHostGroup(), instanceTask.getAgentHostRun());
		//在执行exec()前一步
		if (defAgentGroup == null) {
			return true;
		}
		String instanceDir = getInstanceDir(instanceTask, defAgentGroup.getWorkBaseDir());
		return SSHAsynExecUtils.kill(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword(),
				instanceDir);
	}

	public boolean isRunnable(InstanceTask instanceTask, Integer isForceRun) {
		if (instanceTask == null) {
			return false;
		}
		if (instanceTask.getIsValid().intValue() == 0) {
			return false;
		}
		if (isDone(instanceTask)) {
			return false;
		}
		if (isForceRun == 0) {
			return ServiceBeanFactory.getInstanceTaskService().isSuccessPreInstance(instanceTask.getTaskId(), instanceTask.getTaskDate());
		}
		return true;
	}

	public static boolean isCanNext(InstanceTask instanceTask) {
		if (isInvalidInstanceTask(instanceTask)) {
			return false;
		}
		return StringUtils.equals(instanceTask.getStatus(), Status.SUCCESS.name())
				|| (StringUtils.equals(instanceTask.getStatus(), Status.FAIL.name()) && instanceTask.getIsIgnoreError() == 1 && instanceTask.getRetriedNum() == instanceTask.getMaxRetryNum());
	}

	public static boolean isDone(InstanceTask instanceTask) {
		return StringUtils.equals(instanceTask.getStatus(), Status.KILL.name()) || StringUtils.equals(instanceTask.getStatus(), Status.SUCCESS.name())
				|| (StringUtils.equals(instanceTask.getStatus(), Status.FAIL.name()) && instanceTask.getRetriedNum() == instanceTask.getMaxRetryNum());
	}

	public static boolean isRunning(InstanceTask instanceTask) {
		return StringUtils.equals(instanceTask.getStatus(), Status.RUNNING.name());
	}

	private void retriedNumAdd1(InstanceTask instanceTask) {
		int retriedNum = instanceTask.getRetriedNum() + 1;
		logger.info("retriedNum={}, {}", retriedNum, instanceTaskKeyDetail);
		instanceTask.setRetriedNum(retriedNum);
		instanceTask.setStatus(Status.READY.name());
		instanceTask.setReadyTime(new Date());
		instanceTask.setStartTime(null);
		instanceTask.setEndTime(null);
		instanceTask.setAgentHostRun("");
		instanceTask.setUpdateTime(new Date());
		ServiceBeanFactory.getInstanceTaskService().update(instanceTask);
		InstanceTaskCacheFactory.put(instanceTask);
	}

	public void setReadyStatus(InstanceTask instanceTask) {
		// 考虑重启
		if (StringUtils.equals(instanceTask.getStatus(), Status.INIT.name())) {
			instanceTask.setStatus(Status.READY.name());
			instanceTask.setReadyTime(new Date());
			instanceTask.setUpdateTime(new Date());
			ServiceBeanFactory.getInstanceTaskService().update(instanceTask);
			InstanceTaskCacheFactory.put(instanceTask);
		}
	}

	/**
	 * 设置任务实例开始状态
	 * 
	 * @param instanceTask
	 */
	private void setStartStatus(InstanceTask instanceTask) {
		// 考虑重启
		if (!StringUtils.equals(instanceTask.getStatus(), Status.RUNNING.name())) {
			instanceTask.setStartTime(new Date());
			instanceTask.setEndTime(null);
			instanceTask.setAgentHostRun("");
		}
		instanceTask.setStatus(Status.RUNNING.name());
		instanceTask.setUpdateTime(new Date());
		ServiceBeanFactory.getInstanceTaskService().update(instanceTask);
		InstanceTaskCacheFactory.put(instanceTask);
	}

	/**
	 * 设置任务实例完成状态
	 * 
	 * @param instanceTask
	 */
	public static void setEndStatus(InstanceTask instanceTask, String status, Integer isValid) {
		Date date = new Date();
		// kill发生时
		if (instanceTask.getReadyTime() == null) {
			instanceTask.setReadyTime(date);
		}
		if (instanceTask.getStartTime() == null) {
			instanceTask.setStartTime(date);
		}
		instanceTask.setEndTime(date);
		instanceTask.setStatus(status);
		instanceTask.setUpdateTime(new Date());
		if (isValid == 0) {
			instanceTask.setIsValid(isValid);
		}
		ServiceBeanFactory.getInstanceTaskService().update(instanceTask);
		InstanceTaskCacheFactory.put(instanceTask);
	}

	public static String getTaskPluginDir(String workBaseDir) {
		return workBaseDir + "/plugins";
	}
	
	public static String getInstanceBaseDir(String workBaseDir) {
		return workBaseDir + "/instances";
	}

	@Override
	public String toString() {
		return instanceTaskKeyDetail.toString();
	}

	/**
	 * 任务提交至执行线程池后，先后执行的顺序：优先级小的，先执行
	 */
	@Override
	public int compareTo(InstanceTaskExecutor o) {
		try {
			InstanceTask curInstanceTask = getInstanceNoLock();
			InstanceTask oInstanceTask = null;
			if (o != null) {
				oInstanceTask = o.getInstanceNoLock();
			}
			
			if (curInstanceTask == null && oInstanceTask == null) {
				return 0;
			} else if (curInstanceTask != null && oInstanceTask == null) {
				return -1;
			} else if (curInstanceTask == null && oInstanceTask != null) {
				return 1;
			}

			// 重启后，先把原来运行中的任务运行
			boolean isCurRun = isRunning(curInstanceTask);
			boolean isORun = isRunning(oInstanceTask);
			if (isCurRun && isORun) {
				return 0;
			} else if (isCurRun && !isORun) {
				return -1;
			} else if (!isCurRun && isORun) {
				return 1;
			}

			boolean isCurVirtual = curInstanceTask.isVirtualTask();
			boolean isOVirtual = oInstanceTask.isVirtualTask();
			if (isCurVirtual && isOVirtual) {
				return 0;
			} else if (isCurVirtual && !isOVirtual) {
				return -1;
			} else if (!isCurVirtual && isOVirtual) {
				return 1;
			}

			int compareTdateResult = DateUtils2.dateStr(curInstanceTask.getTaskDate()).compareTo(DateUtils2.dateStr(oInstanceTask.getTaskDate()));
			// 日期大的先跑（相当于降低重跑的任务的优先级）
			if (compareTdateResult != 0) {
				return 0 - compareTdateResult;
			}

			int comaprePriorityResult = curInstanceTask.getPriority().intValue() - oInstanceTask.getPriority().intValue();
			// 优先级低的先跑
			if (comaprePriorityResult != 0) {
				return comaprePriorityResult;
			}

			if (curInstanceTask.getReadyTime() == null && oInstanceTask.getReadyTime() == null) {
				return 0;
			} else if (curInstanceTask.getReadyTime() != null && oInstanceTask.getReadyTime() == null) {
				return -1;
			} else if (curInstanceTask.getReadyTime() == null && oInstanceTask.getReadyTime() != null) {
				return 1;
			} else {
				// 先准备好的先跑
				return DateUtils2.dateStr(curInstanceTask.getReadyTime()).compareTo(DateUtils2.dateStr(oInstanceTask.getReadyTime()));
			}
		} catch (Throwable e) {
			logger.error("compareTo error", e);
		}
		return 0;
	}

}
