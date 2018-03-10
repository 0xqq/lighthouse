package com.huya.lighthouse.model.po.instance;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.DateUtils2;

/**
 * tableName: instance_task [InstanceTask]
 * 
 */
public class InstanceTask extends DefTask {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 任务数据日期 db_column: task_date
	 */
	private java.util.Date taskDate;

	/**
	 * 实例id db_column: instance_id
	 */
	private java.lang.String instanceId;

	/**
	 * 实际在哪个Agent运行该任务 db_column: agent_host_run
	 */
	private java.lang.String agentHostRun;

	/**
	 * 在哪些Agent运行过该任务，用分号分隔 db_column: agent_host_run_his
	 */
	private java.lang.String agentHostRunHis;

	/**
	 * 已经重试的次数 db_column: retried_num
	 */
	private java.lang.Integer retriedNum = 0;

	/**
	 * 准备好要运行的时间 db_column: ready_time
	 */
	private java.util.Date readyTime;

	/**
	 * 开始时间 db_column: start_time
	 */
	private java.util.Date startTime;

	/**
	 * 结束时间 db_column: end_time
	 */
	private java.util.Date endTime;

	/**
	 * 状态 db_column: status
	 */
	private java.lang.String status;

	/**
	 * 是否只运行自己,不触发后续依赖, 1是, 0否db_column: is_self_run
	 */
	private Integer isSelfRun;

	/**
	 * 是否强制运行, 1是, 0否, 1是, 0否db_column: is_force_run
	 */
	private Integer isForceRun;

	/**
	 * 上下文信息 db_column: context
	 */
	private java.lang.String context;

	private java.lang.String scheduleLog;

	private Map<String, Object> contextMap = new HashMap<String, Object>();

	public java.util.Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(java.util.Date taskDate) {
		this.taskDate = taskDate;
	}

	public java.lang.String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(java.lang.String instanceId) {
		this.instanceId = instanceId;
	}

	public java.lang.String getAgentHostRun() {
		return agentHostRun;
	}

	public void setAgentHostRun(java.lang.String agentHostRun) {
		this.agentHostRun = agentHostRun;
	}

	public java.lang.String getAgentHostRunHis() {
		return agentHostRunHis;
	}

	public void setAgentHostRunHis(java.lang.String agentHostRunHis) {
		this.agentHostRunHis = agentHostRunHis;
	}

	public java.lang.Integer getRetriedNum() {
		return retriedNum;
	}

	public void setRetriedNum(java.lang.Integer retriedNum) {
		this.retriedNum = retriedNum;
	}

	public java.util.Date getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(java.util.Date readyTime) {
		this.readyTime = readyTime;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public Integer getIsSelfRun() {
		return isSelfRun;
	}

	public void setIsSelfRun(Integer isSelfRun) {
		this.isSelfRun = isSelfRun;
	}

	public Integer getIsForceRun() {
		return isForceRun;
	}

	public void setIsForceRun(Integer isForceRun) {
		this.isForceRun = isForceRun;
	}

	public java.lang.String getContext() {
		return context;
	}

	public void setContext(java.lang.String context) {
		this.context = context;
	}

	public Map<String, Object> getContextMap() {
		return contextMap;
	}

	public void setContextMap(Map<String, Object> contextMap) {
		this.contextMap = contextMap;
	}

	public java.lang.String getScheduleLog() {
		return scheduleLog;
	}

	public void setScheduleLog(java.lang.String scheduleLog) {
		this.scheduleLog = scheduleLog;
	}

	public boolean isVirtualTask() {
		return StringUtils.equals(this.getTaskType(), TaskType.VIRTUAL.name()) || StringUtils.equals(this.getTaskType(), TaskType.CRON.name());
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(getTaskId()).append(DateUtils2.dateStr(getTaskDate())).append(getInstanceId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof InstanceTask == false)
			return false;
		InstanceTask other = (InstanceTask) obj;
		return new EqualsBuilder().append(getTaskId(), getTaskId()).append(DateUtils2.dateStr(getTaskDate()), DateUtils2.dateStr(other.getTaskDate())).append(getInstanceId(), other.getInstanceId()).isEquals();
	}
}
