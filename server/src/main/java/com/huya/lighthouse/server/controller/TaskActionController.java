package com.huya.lighthouse.server.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.type.TypeReference;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskKey;
import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.server.TaskAction;
import com.huya.lighthouse.server.executor.LeaderSelector;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.util.CronExpUtils;
import com.huya.lighthouse.util.JsonUtils;

/**
 * 
 * 
 */
@Controller
public class TaskActionController {

	private static Logger logger = LoggerFactory.getLogger(TaskActionController.class);

	/** binder用于bean属性的设置 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyyMMddHHmmss"), true));
	}

	@RequestMapping("/run.do")
	public void run(HttpServletResponse response, Date startDate, Date endDate, String taskIds, Integer isSelfRun, Integer isForceRun) throws Exception {
		try {
			logger.info("/run.do: {}, {}, {}, {}, {}", new Object[] { DateFormatUtils.format(startDate, "yyyyMMddHHmmss"), DateFormatUtils.format(endDate, "yyyyMMddHHmmss"), taskIds, isSelfRun, isForceRun });
			if (endDate.getTime() > System.currentTimeMillis()) {
				throw new Exception("不允许调度大于当前时间的实例");
			}
			
			LeaderSelector.checkIsLeader();
			Date startDPreMillis = DateUtils.addMilliseconds(startDate, -1);
			Date endDPreMillis = DateUtils.addMilliseconds(endDate, 1);
	
			String[] taskIdStrArr = StringUtils.split(taskIds, ",");
			if (taskIdStrArr == null) {
				return;
			}
			String instanceId = UUID.randomUUID().toString();
			for (String taskIdStr : taskIdStrArr) {
				if (StringUtils.isBlank(taskIdStr)) {
					continue;
				}
				taskIdStr = StringUtils.trim(taskIdStr);
				Integer taskId = Integer.parseInt(taskIdStr);
				DefTask defTask = ServiceBeanFactory.getDefTaskService().getById(taskId);
				if (defTask == null) {
					continue;
				}
				if (StringUtils.equals(defTask.getTaskType(), TaskType.CRON.name())) {
					throw new Exception("不允许手动调度定时器任务：taskId=" + defTask.getTaskId() + ", taskName=" + defTask.getTaskName());
				}
				CronExpression cronExp = new CronExpression(defTask.getExecCronExp());
				List<Date> taskDateList = CronExpUtils.getTimeBetween(cronExp, startDPreMillis, endDPreMillis, 0);
				if (taskDateList == null || taskDateList.size() == 0) {
					throw new Exception("taskId=" + taskId + "，[" + DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss") + ", " + DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss") + "]范围不合法，没有可执行的实例！");
				}
				for (Date taskDate : taskDateList) {
					TaskAction.run(taskId, taskDate, instanceId, isSelfRun, isForceRun);
				}
			}
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/kill.do")
	public void kill(HttpServletResponse response, String listStr) throws Exception {
		try {
			logger.info("/kill.do: " + listStr);
			LeaderSelector.checkIsLeader();
			List<InstanceTaskKey> instanceTaskKeyList = JsonUtils.decode(listStr, new TypeReference<List<InstanceTaskKey>>() {
			});
			for (InstanceTaskKey instanceTaskKey : instanceTaskKeyList) {
				TaskAction.kill(instanceTaskKey.getTaskId(), instanceTaskKey.getTaskDate(), instanceTaskKey.getInstanceId(), 1);
			}
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/setSuccess.do")
	public void setSuccess(HttpServletResponse response, String listStr) throws Exception {
		try {
			logger.info("/setSuccess.do: " + listStr);
			LeaderSelector.checkIsLeader();
			List<InstanceTaskKey> instanceTaskKeyList = JsonUtils.decode(listStr, new TypeReference<List<InstanceTaskKey>>() {
			});
			for (InstanceTaskKey instanceTaskKey : instanceTaskKeyList) {
				TaskAction.setSuccess(instanceTaskKey.getTaskId(), instanceTaskKey.getTaskDate(), instanceTaskKey.getInstanceId());
			}
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/rebuildInit.do")
	public void rebuildInit(HttpServletResponse response, Integer taskId) throws Exception {
		try {
			logger.info("/rebuildInit.do: " + taskId);
			LeaderSelector.checkIsLeader();
			TaskAction.rebuildInit(taskId);
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/invalidInit.do")
	public void invalidInit(HttpServletResponse response, Integer taskId) throws Exception {
		try {
			logger.info("/invalidInit.do: " + taskId);
			LeaderSelector.checkIsLeader();
			TaskAction.invalidInit(taskId);
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/addCronTask.do")
	public void addCronTask(HttpServletResponse response, Integer taskId, String cronExp) throws Exception {
		try {
			logger.info("/addCronTask.do: {}, {}", taskId, cronExp);
			LeaderSelector.checkIsLeader();
			TaskAction.addCronTask(taskId, cronExp);
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/addCronMonitorBegin.do")
	public void addCronMonitorBegin(HttpServletResponse response, String cronExps) throws Exception {
		try {
			logger.info("/addCronMonitorBegin.do: " + cronExps);
			LeaderSelector.checkIsLeader();
			if (StringUtils.isBlank(cronExps)) {
				throw new Exception("cronExps cannot be null");
			}
			String[] cronExpArr = StringUtils.split(cronExps, ";");
			for (String cronExp : cronExpArr) {
				if (StringUtils.isBlank(cronExp)) {
					continue;
				}
				TaskAction.addCronMonitorBegin(cronExp);
			}
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/reBuildDefAgentGroupFactory.do")
	public void reBuildDefAgentGroupFactory(HttpServletResponse response) throws Exception {
		try {
			logger.info("/reBuildDefAgentGroupFactory.do");
			LeaderSelector.checkIsLeader();
			TaskAction.reBuildDefAgentGroupFactory();
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/reBuildDefParamFactory.do")
	public void reBuildDefParamFactory(HttpServletResponse response) throws Exception {
		try {
			logger.info("/reBuildDefParamFactory.do");
			LeaderSelector.checkIsLeader();
			TaskAction.reBuildDefParamFactory();
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}

	@RequestMapping("/updateQueueSize.do")
	public void updateQueueSize(HttpServletResponse response, Integer queueId, Integer queueSize) throws Exception {
		try {
			logger.info("/updateQueueSize.do: {}, {}", queueId, queueSize);
			LeaderSelector.checkIsLeader();
			TaskAction.updateQueueSize(queueId, queueSize);
			response.getWriter().print("200");
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(500);
			response.getWriter().print(e.getMessage());
		}
	}
}
