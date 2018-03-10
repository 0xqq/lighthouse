package com.huya.lighthouse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huya.lighthouse.dao.InstanceTaskLogDao;
import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskLog;
import com.huya.lighthouse.model.type.Status;
import com.huya.lighthouse.service.DefAgentGroupService;
import com.huya.lighthouse.service.InstanceTaskLogService;
import com.huya.lighthouse.service.InstanceTaskService;
import com.huya.lighthouse.util.SSHUtils;

/**
 * [InstanceTaskLog] 的业务操作实现类
 * 
 */
@Service("instanceTaskLogService")
public class InstanceTaskLogServiceImpl implements InstanceTaskLogService {

	protected static final Logger log = LoggerFactory.getLogger(InstanceTaskLogServiceImpl.class);

	@Autowired
	private InstanceTaskLogDao instanceTaskLogDao;
	
	@Autowired
	private DefAgentGroupService defAgentGroupService;
	
	@Autowired
	private InstanceTaskService instanceTaskService;
	
	private boolean isProd = StringUtils.equals(System.getenv("DWENV"), "prod");
	
	private static Cache<String, DefAgentGroup> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.HOURS).expireAfterAccess(1, TimeUnit.HOURS).build();

	/**
	 * 创建InstanceTaskLog
	 **/
	public InstanceTaskLog create(InstanceTaskLog instanceTaskLog) {
		Assert.notNull(instanceTaskLog, "'instanceTaskLog' must be not null");
		instanceTaskLogDao.insert(instanceTaskLog);
		return instanceTaskLog;
	}

	/**
	 * 更新InstanceTaskLog
	 **/
	public InstanceTaskLog update(InstanceTaskLog instanceTaskLog) {
		Assert.notNull(instanceTaskLog, "'instanceTaskLog' must be not null");
		instanceTaskLogDao.update(instanceTaskLog);
		return instanceTaskLog;
	}

	/**
	 * 删除InstanceTaskLog
	 **/
	public void removeById(int taskId, Date taskDate, String instanceId, long logId) {
		instanceTaskLogDao.deleteById(taskId, taskDate, instanceId, logId);
	}

	/**
	 * 根据ID得到InstanceTaskLog
	 **/
	public InstanceTaskLog getById(int taskId, Date taskDate, String instanceId, long logId) {
		return instanceTaskLogDao.getById(taskId, taskDate, instanceId, logId);
	}

	@Override
	public List<InstanceTaskLog> getInstanceTaskLogs(int taskId, Date taskDate, String instanceId) {
		return instanceTaskLogDao.getInstanceTaskLogs(taskId, taskDate, instanceId);
	}

	@Override
	public void getLog(OutputStream outputStream, int taskId, Date taskDate, String instanceId) throws Exception {
		if (outputStream == null) {
			throw new Exception("outputStream is null");
		}
		List<InstanceTaskLog> instanceTaskLogs = getInstanceTaskLogs(taskId, taskDate, instanceId);
		if (instanceTaskLogs == null || instanceTaskLogs.size() == 0) {
			InstanceTask instanceTask = instanceTaskService.getById(taskId, taskDate, instanceId);
			if (instanceTask == null) {
				IOUtils.write("该任务实例不存在。", outputStream);
				return;
			}
			String status = instanceTask.getStatus();
			if (StringUtils.equals(status, Status.INIT.name()) || (StringUtils.equals(status, Status.READY.name()) && instanceTask.getRetriedNum() == 0)) {
				IOUtils.write("该任务实例未开始运行，暂无日志。", outputStream);
				return;
			}
			IOUtils.write("该任务实例无日志。", outputStream);
			return;
		}
		for (InstanceTaskLog instanceTaskLog : instanceTaskLogs) {
			String content = instanceTaskLog.getContent();
			if (StringUtils.isNotBlank(content)) {
				IOUtils.write("\n=====================================================server log=====================================================\n", outputStream);
				ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
				IOUtils.copy(bais, outputStream);
				bais = null;
			} else {
				DefAgentGroup defAgentGroupTarget = cache.getIfPresent(instanceTaskLog.getAgentHostRun());
				if (defAgentGroupTarget == null) {
					List<DefAgentGroup> defAgentGroups = defAgentGroupService.getAllValid();
					if (defAgentGroups != null) {
						for (DefAgentGroup defAgentGroup : defAgentGroups) {
							cache.put(defAgentGroup.getAgentHost(), defAgentGroup);
						}
					}
					defAgentGroupTarget = cache.getIfPresent(instanceTaskLog.getAgentHostRun());
				}
				if (defAgentGroupTarget == null || !isProd) {
					continue;
				}
				String host = defAgentGroupTarget.getAgentHost();
				int port = defAgentGroupTarget.getAgentPort();
				String user = defAgentGroupTarget.getAgentUser();
				String privateKey = defAgentGroupTarget.getAgentPrivateKey();
				String password = defAgentGroupTarget.getAgentPassword();
				IOUtils.write("\n=====================================================agent log: " + host + "=====================================================\n", outputStream);
				SSHUtils.sftpGet(host, port, user, privateKey, password, instanceTaskLog.getLogPath(), outputStream);
			}
		}
	}
}
