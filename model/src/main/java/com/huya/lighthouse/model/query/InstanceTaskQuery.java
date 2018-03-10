package com.huya.lighthouse.model.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.model.query.page.PageQuery;

/**
 */
public class InstanceTaskQuery extends PageQuery implements Serializable {
	private static final long serialVersionUID = 3148176768559230877L;

	/** 任务数据日期 */
	private java.util.Date taskDateBegin;
	private java.util.Date taskDateEnd;
	/** 任务id */
	private java.lang.Integer taskId;
	/** 实例id */
	private java.lang.String instanceId;
	/** 所属项目id */
	private java.lang.Integer catalogId;
	/** 任务名称 */
	private java.lang.String taskName;
	/** 任务类型: 定时器, 虚拟任务... */
	private java.lang.String taskType;
	/** 任务执行插件 */
	private java.lang.String taskPlugin;
	/** 任务内容 */
	private java.lang.String taskBody;
	/** 执行频次quartz表达式 */
	private java.lang.String execCronExp;
	/** 将要在哪个Agent组运行该任务 */
	private java.lang.String agentHostGroup;
	/** 实际在哪个Agent运行该任务 */
	private java.lang.String agentHostRun;
	/** 在哪些Agent运行过该任务，用分号分隔 */
	private java.lang.String agentHostRunHis;
	/** linux运行用户 */
	private java.lang.String linuxRunUser;
	/** 所属运行队列 */
	private java.lang.Integer queueId;
	/** 优先级 */
	private java.lang.Integer priority;
	/** 同时运行的最大实例数 */
	private java.lang.Integer maxRunNum;
	/** 运行耗时到达该值，任务就kill掉 */
	private java.lang.Integer maxRunSec;
	/** 任务失败最大重试次数 */
	private java.lang.Integer maxRetryNum;
	/** 已经重试的次数 */
	private java.lang.Integer retriedNum;
	/** 重试间隔(秒) */
	private java.lang.Integer retryInterval;
	/** 最终失败是否可忽略, 1可忽略, 0不可忽略 */
	private Integer isIgnoreError;
	/** 是否一次性任务, 1是, 0否 */
	private Integer isOneTimes;
	/** 下线时间 */
	private java.util.Date offlineTimeBegin;
	private java.util.Date offlineTimeEnd;
	/** 准备好要运行的时间 */
	private java.util.Date readyTimeBegin;
	private java.util.Date readyTimeEnd;
	/** 开始时间 */
	private java.util.Date startTimeBegin;
	private java.util.Date startTimeEnd;
	/** 结束时间 */
	private java.util.Date endTimeBegin;
	private java.util.Date endTimeEnd;
	/** 状态 */
	private java.lang.String status;
	/** 是否只运行自己,不触发后续依赖, 1是, 0否 */
	private Integer isSelfRun;
	/** 是否强制运行, 1是, 0否 */
	private Integer isForceRun;
	/** 上下文信息 */
	private java.lang.String context;
	/** 初始化日志 */
	private java.lang.String scheduleLog;
	/** 备注 */
	private java.lang.String remarks;
	/** 是否还可用，1可用，0已下线不可用 */
	private Integer isValid;
	/** 创建时间 */
	private java.util.Date createTimeBegin;
	private java.util.Date createTimeEnd;
	/** 更新时间 */
	private java.util.Date updateTimeBegin;
	private java.util.Date updateTimeEnd;
	/** 创建用户 */
	private java.lang.String createUser;
	/** 更新用户 */
	private java.lang.String updateUser;

	public java.util.Date getTaskDateBegin() {
		return this.taskDateBegin;
	}

	public void setTaskDateBegin(java.util.Date value) {
		this.taskDateBegin = value;
	}

	public java.util.Date getTaskDateEnd() {
		return this.taskDateEnd;
	}

	public void setTaskDateEnd(java.util.Date value) {
		this.taskDateEnd = value;
	}

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}

	public java.lang.String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(java.lang.String value) {
		this.instanceId = value;
	}

	public java.lang.Integer getCatalogId() {
		return this.catalogId;
	}

	public void setCatalogId(java.lang.Integer value) {
		this.catalogId = value;
	}

	public java.lang.String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(java.lang.String value) {
		this.taskName = value;
	}

	public java.lang.String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(java.lang.String value) {
		this.taskType = value;
	}

	public java.lang.String getTaskPlugin() {
		return this.taskPlugin;
	}

	public void setTaskPlugin(java.lang.String value) {
		this.taskPlugin = value;
	}

	public java.lang.String getTaskBody() {
		return this.taskBody;
	}

	public void setTaskBody(java.lang.String value) {
		this.taskBody = value;
	}

	public java.lang.String getExecCronExp() {
		return this.execCronExp;
	}

	public void setExecCronExp(java.lang.String value) {
		this.execCronExp = value;
	}

	public java.lang.String getAgentHostGroup() {
		return this.agentHostGroup;
	}

	public void setAgentHostGroup(java.lang.String value) {
		this.agentHostGroup = value;
	}

	public java.lang.String getAgentHostRun() {
		return this.agentHostRun;
	}

	public void setAgentHostRun(java.lang.String value) {
		this.agentHostRun = value;
	}

	public java.lang.String getAgentHostRunHis() {
		return this.agentHostRunHis;
	}

	public void setAgentHostRunHis(java.lang.String value) {
		this.agentHostRunHis = value;
	}

	public java.lang.String getLinuxRunUser() {
		return this.linuxRunUser;
	}

	public void setLinuxRunUser(java.lang.String value) {
		this.linuxRunUser = value;
	}

	public java.lang.Integer getQueueId() {
		return this.queueId;
	}

	public void setQueueId(java.lang.Integer value) {
		this.queueId = value;
	}

	public java.lang.Integer getPriority() {
		return this.priority;
	}

	public void setPriority(java.lang.Integer value) {
		this.priority = value;
	}

	public java.lang.Integer getMaxRunNum() {
		return this.maxRunNum;
	}

	public void setMaxRunNum(java.lang.Integer value) {
		this.maxRunNum = value;
	}

	public java.lang.Integer getMaxRunSec() {
		return this.maxRunSec;
	}

	public void setMaxRunSec(java.lang.Integer value) {
		this.maxRunSec = value;
	}

	public java.lang.Integer getMaxRetryNum() {
		return this.maxRetryNum;
	}

	public void setMaxRetryNum(java.lang.Integer value) {
		this.maxRetryNum = value;
	}

	public java.lang.Integer getRetriedNum() {
		return this.retriedNum;
	}

	public void setRetriedNum(java.lang.Integer value) {
		this.retriedNum = value;
	}

	public java.lang.Integer getRetryInterval() {
		return this.retryInterval;
	}

	public void setRetryInterval(java.lang.Integer value) {
		this.retryInterval = value;
	}

	public Integer getIsIgnoreError() {
		return this.isIgnoreError;
	}

	public void setIsIgnoreError(Integer value) {
		this.isIgnoreError = value;
	}

	public Integer getIsOneTimes() {
		return this.isOneTimes;
	}

	public void setIsOneTimes(Integer value) {
		this.isOneTimes = value;
	}

	public java.util.Date getOfflineTimeBegin() {
		return this.offlineTimeBegin;
	}

	public void setOfflineTimeBegin(java.util.Date value) {
		this.offlineTimeBegin = value;
	}

	public java.util.Date getOfflineTimeEnd() {
		return this.offlineTimeEnd;
	}

	public void setOfflineTimeEnd(java.util.Date value) {
		this.offlineTimeEnd = value;
	}

	public java.util.Date getReadyTimeBegin() {
		return this.readyTimeBegin;
	}

	public void setReadyTimeBegin(java.util.Date value) {
		this.readyTimeBegin = value;
	}

	public java.util.Date getReadyTimeEnd() {
		return this.readyTimeEnd;
	}

	public void setReadyTimeEnd(java.util.Date value) {
		this.readyTimeEnd = value;
	}

	public java.util.Date getStartTimeBegin() {
		return startTimeBegin;
	}

	public void setStartTimeBegin(java.util.Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}

	public java.util.Date getStartTimeEnd() {
		return startTimeEnd;
	}

	public void setStartTimeEnd(java.util.Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}

	public java.util.Date getEndTimeBegin() {
		return endTimeBegin;
	}

	public void setEndTimeBegin(java.util.Date endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}

	public java.util.Date getEndTimeEnd() {
		return endTimeEnd;
	}

	public void setEndTimeEnd(java.util.Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}

	public java.lang.String getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.String value) {
		this.status = value;
	}

	public Integer getIsSelfRun() {
		return this.isSelfRun;
	}

	public void setIsSelfRun(Integer value) {
		this.isSelfRun = value;
	}

	public Integer getIsForceRun() {
		return this.isForceRun;
	}

	public void setIsForceRun(Integer value) {
		this.isForceRun = value;
	}

	public java.lang.String getContext() {
		return this.context;
	}

	public void setContext(java.lang.String value) {
		this.context = value;
	}

	public java.lang.String getScheduleLog() {
		return this.scheduleLog;
	}

	public void setScheduleLog(java.lang.String value) {
		this.scheduleLog = value;
	}

	public java.lang.String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	public Integer getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Integer value) {
		this.isValid = value;
	}

	public java.util.Date getCreateTimeBegin() {
		return this.createTimeBegin;
	}

	public void setCreateTimeBegin(java.util.Date value) {
		this.createTimeBegin = value;
	}

	public java.util.Date getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setCreateTimeEnd(java.util.Date value) {
		this.createTimeEnd = value;
	}

	public java.util.Date getUpdateTimeBegin() {
		return this.updateTimeBegin;
	}

	public void setUpdateTimeBegin(java.util.Date value) {
		this.updateTimeBegin = value;
	}

	public java.util.Date getUpdateTimeEnd() {
		return this.updateTimeEnd;
	}

	public void setUpdateTimeEnd(java.util.Date value) {
		this.updateTimeEnd = value;
	}

	public java.lang.String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(java.lang.String value) {
		this.createUser = value;
	}

	public java.lang.String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(java.lang.String value) {
		this.updateUser = value;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
