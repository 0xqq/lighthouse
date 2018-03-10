package com.huya.lighthouse.server.instance.initiator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.def.DefTaskDepend;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;
import com.huya.lighthouse.model.type.DependType;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.util.CronExpUtils;
import com.huya.lighthouse.util.DateUtils2;

public class InstanceTaskDependInitiator {

	private static Logger logger = LoggerFactory.getLogger(InstanceTaskDependInitiator.class);

	public static List<InstanceTaskDepend> initPre(Integer taskId, Date taskDate, String instanceId) {
		List<InstanceTaskDepend> result = new ArrayList<InstanceTaskDepend>();
		List<DefTaskDepend> preDefTaskDependList = ServiceBeanFactory.getDefTaskDependService().getByPostId(taskId, 1);
		if (preDefTaskDependList == null) {
			return result;
		}
		for (DefTaskDepend preDefTaskDepend : preDefTaskDependList) {
			DependType dependType = Enum.valueOf(DependType.class, preDefTaskDepend.getDependType());
			switch (dependType) {
			case SAMELEVEL: {
				InstanceTaskDepend instanceTaskDepend = initInstanceTaskDepend(instanceId, taskDate, taskDate, preDefTaskDepend);
				result.add(instanceTaskDepend);
				break;
			}
			case SELF: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> beforeDate1 = CronExpUtils.getTimeBeforeN(cronExp, taskDate, 2);
				if (beforeDate1 == null || beforeDate1.size()!=2) {
					logger.error("beforeDate1 is invalid of taskId={}, taskDate={}, dependN={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate), preDefTaskDepend.getDependN() });
					continue;
				}
				beforeDate1.remove(0);
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, beforeDate1, preDefTaskDepend));
				break;
			}
			case SLIDE: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> beforeDateN = CronExpUtils.getTimeBeforeN(cronExp, taskDate, preDefTaskDepend.getDependN());
				if (beforeDateN == null) {
					logger.error("beforeDateN is null of taskId={}, taskDate={}, dependN={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate), preDefTaskDepend.getDependN() });
					continue;
				}
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, beforeDateN, preDefTaskDepend));
				break;
			}
			case HOUR: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> timeInHour = CronExpUtils.getTimeInHour(cronExp, taskDate);
				if (timeInHour == null) {
					logger.error("timeInHour is null of taskId={}, taskDate={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate) });
					continue;
				}
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, timeInHour, preDefTaskDepend));
				break;
			}
			case DAY: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> timeInDate = CronExpUtils.getTimeInDate(cronExp, taskDate);
				if (timeInDate == null) {
					logger.error("timeInDate is null of taskId={}, taskDate={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate) });
					continue;
				}
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, timeInDate, preDefTaskDepend));
				break;
			}
			case WEEK: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> timeInWeek = CronExpUtils.getTimeInWeek(cronExp, taskDate, null);
				if (timeInWeek == null) {
					logger.error("timeInWeek is null of taskId={}, taskDate={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate) });
					continue;
				}
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, timeInWeek, preDefTaskDepend));
				break;
			}
			case MONTH: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> timeInMonth = CronExpUtils.getTimeInMonth(cronExp, taskDate, null);
				if (timeInMonth == null) {
					logger.error("timeInMonth is null of taskId={}, taskDate={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate) });
					continue;
				}
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, timeInMonth, preDefTaskDepend));
				break;
			}
			case QUARTER: {
				CronExpression cronExp = getCronExp(preDefTaskDepend.getPreTaskId());
				List<Date> timeInQuarter = CronExpUtils.getTimeInQuarter(cronExp, taskDate, null);
				if (timeInQuarter == null) {
					logger.error("timeInQuarter is null of taskId={}, taskDate={}", new Object[] { preDefTaskDepend.getPreTaskId(), DateUtils2.dateStr(taskDate) });
					continue;
				}
				result.addAll(initInstanceTaskDependPre(instanceId, taskDate, timeInQuarter, preDefTaskDepend));
				break;
			}
			}
		}
		return result;
	}

	public static List<InstanceTaskDepend> initPost(Integer preTaskId, Date preTaskDate, String instanceId) {
		List<InstanceTaskDepend> result = new ArrayList<InstanceTaskDepend>();
		List<DefTaskDepend> postDefTaskDependList = ServiceBeanFactory.getDefTaskDependService().getByPreId(preTaskId, 1);
		if (postDefTaskDependList != null) {
			Date yyyyMMddToday = DateUtils2.clear2HourLevel(new Date());
			Date yyyyMMddTomorrow = DateUtils.addDays(yyyyMMddToday, 1);
			for (DefTaskDepend postDefTaskDepend : postDefTaskDependList) {
				DependType dependType = Enum.valueOf(DependType.class, postDefTaskDepend.getDependType());
				switch (dependType) {
				case SAMELEVEL: {
					InstanceTaskDepend instanceTaskDepend = initInstanceTaskDepend(instanceId, preTaskDate, preTaskDate, postDefTaskDepend);
					result.add(instanceTaskDepend);
					break;
				}
				case SELF: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getTaskId());
					Date afterDate = cronExp.getTimeAfter(preTaskDate);
					if (afterDate.getTime() < yyyyMMddTomorrow.getTime()) {
						List<Date> afterDate1 = Arrays.asList(afterDate);
						result.addAll(initInstanceTaskDependPost(instanceId, afterDate1, preTaskDate, postDefTaskDepend));
					}
					break;
				}
				case SLIDE: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getTaskId());
					Date taskDatePreMillis = DateUtils.addMilliseconds(preTaskDate, -1);
					List<Date> afterDateN = CronExpUtils.getTimeBetween(cronExp, taskDatePreMillis, yyyyMMddTomorrow, postDefTaskDepend.getDependN());
					result.addAll(initInstanceTaskDependPost(instanceId, afterDateN, preTaskDate, postDefTaskDepend));
					break;
				}
				case HOUR: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getTaskId());
					List<Date> timeInHour = CronExpUtils.getTimeInHour(cronExp, preTaskDate);
					result.addAll(initInstanceTaskDependPost(instanceId, timeInHour, preTaskDate, postDefTaskDepend));
					break;
				}
				case DAY: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getTaskId());
					List<Date> timeInDate = CronExpUtils.getTimeInDate(cronExp, preTaskDate);
					result.addAll(initInstanceTaskDependPost(instanceId, timeInDate, preTaskDate, postDefTaskDepend));
					break;
				}
				case WEEK: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getTaskId());
					List<Date> timeInWeek = CronExpUtils.getTimeInWeek(cronExp, preTaskDate, yyyyMMddTomorrow);
					result.addAll(initInstanceTaskDependPost(instanceId, timeInWeek, preTaskDate, postDefTaskDepend));
					break;
				}
				case MONTH: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getPreTaskId());
					List<Date> timeInMonth = CronExpUtils.getTimeInMonth(cronExp, preTaskDate, yyyyMMddTomorrow);
					result.addAll(initInstanceTaskDependPost(instanceId, timeInMonth, preTaskDate, postDefTaskDepend));
					break;
				}
				case QUARTER: {
					CronExpression cronExp = getCronExp(postDefTaskDepend.getPreTaskId());
					List<Date> timeInQuarter = CronExpUtils.getTimeInQuarter(cronExp, preTaskDate, yyyyMMddTomorrow);
					result.addAll(initInstanceTaskDependPost(instanceId, timeInQuarter, preTaskDate, postDefTaskDepend));
					break;
				}
				}
			}
		}
		return result;
	}

	private static List<InstanceTaskDepend> initInstanceTaskDependPre(String instanceId, Date taskDate, List<Date> preTaskDateList, DefTaskDepend preDefTaskDepend) {
		List<InstanceTaskDepend> instanceTaskDependList = new ArrayList<InstanceTaskDepend>();
		if (preTaskDateList == null) {
			return instanceTaskDependList;
		}
		for (Date preTaskDate : preTaskDateList) {
			InstanceTaskDepend instanceTaskDepend = initInstanceTaskDepend(instanceId, taskDate, preTaskDate, preDefTaskDepend);
			instanceTaskDependList.add(instanceTaskDepend);
		}
		return instanceTaskDependList;
	}

	private static List<InstanceTaskDepend> initInstanceTaskDependPost(String instanceId, List<Date> taskDateList, Date preTaskDate, DefTaskDepend preDefTaskDepend) {
		List<InstanceTaskDepend> instanceTaskDependList = new ArrayList<InstanceTaskDepend>();
		if (taskDateList == null) {
			return instanceTaskDependList;
		}
		for (Date taskDate : taskDateList) {
			InstanceTaskDepend instanceTaskDepend = initInstanceTaskDepend(instanceId, taskDate, preTaskDate, preDefTaskDepend);
			instanceTaskDependList.add(instanceTaskDepend);
		}
		return instanceTaskDependList;
	}
	
	private static InstanceTaskDepend initInstanceTaskDepend(String instanceId, Date taskDate, Date preTaskDate, DefTaskDepend defTaskDepend) {
		InstanceTaskDepend instanceTaskDepend = new InstanceTaskDepend(defTaskDepend.getTaskId(), taskDate, instanceId, defTaskDepend.getPreTaskId(), preTaskDate);
		instanceTaskDepend.setDependType(defTaskDepend.getDependType());
		instanceTaskDepend.setDependN(defTaskDepend.getDependN());
		instanceTaskDepend.setIsSameAgentRun(defTaskDepend.getIsSameAgentRun());
		instanceTaskDepend.setIsContextDepend(defTaskDepend.getIsContextDepend());
		instanceTaskDepend.setRemarks(defTaskDepend.getRemarks());
		instanceTaskDepend.setIsValid(defTaskDepend.getIsValid());
		instanceTaskDepend.setCreateTime(new Date());
		instanceTaskDepend.setUpdateTime(new Date());
		instanceTaskDepend.setCreateUser(defTaskDepend.getCreateUser());
		instanceTaskDepend.setUpdateUser(defTaskDepend.getUpdateUser());
		return instanceTaskDepend;
	}

	public static CronExpression getCronExp(Integer taskId) {
		DefTask defTask = ServiceBeanFactory.getDefTaskService().getById(taskId);
		if (defTask == null) {
			logger.error("taskId={} does not exists.", taskId);
			return null;
		}
		return getCronExp(defTask.getExecCronExp(), defTask.getTaskId());
	}

	public static CronExpression getCronExp(String cronExpStr, Integer taskId) {
		CronExpression cronExp = null;
		try {
			cronExp = new CronExpression(cronExpStr);
		} catch (Exception e) {
			String msg = String.format("taskId=%s, execCronExp=%s", taskId, cronExpStr);
			logger.error(msg, e);
			return null;
		}
		return cronExp;
	}

}
