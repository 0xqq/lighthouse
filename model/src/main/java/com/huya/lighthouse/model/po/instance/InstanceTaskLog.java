package com.huya.lighthouse.model.po.instance;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.util.DateUtils2;

/**
 * tableName: instance_task_log [InstanceTaskLog]
 */
public class InstanceTaskLog implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 任务id db_column: task_id
	 */

	private java.lang.Integer taskId;

	/**
	 * 任务的数据日期 db_column: task_date
	 */
	private java.util.Date taskDate;

	/**
	 * 实例id db_column: instance_id
	 */
	private java.lang.String instanceId;

	/**
	 * 日志id db_column: log_id
	 */
	private java.lang.Long logId;

	/**
	 * 实际在哪个Agent运行该任务 db_column: agent_host_run
	 */
	private java.lang.String agentHostRun;

	/**
	 * 日志路径 db_column: log_path
	 */
	private String logPath;

	/**
	 * 日志内容 db_column: content
	 */
	private java.lang.String content;

	public InstanceTaskLog() {
	}

	public InstanceTaskLog(java.lang.Integer taskId, java.util.Date taskDate, java.lang.String instanceId, java.lang.Long logId) {
		this.taskId = taskId;
		this.taskDate = taskDate;
		this.instanceId = instanceId;
		this.logId = logId;
	}

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}

	public java.util.Date getTaskDate() {
		return this.taskDate;
	}

	public void setTaskDate(java.util.Date value) {
		this.taskDate = value;
	}

	public java.lang.String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(java.lang.String value) {
		this.instanceId = value;
	}

	public java.lang.Long getLogId() {
		return this.logId;
	}

	public void setLogId(java.lang.Long value) {
		this.logId = value;
	}

	public java.lang.String getAgentHostRun() {
		return this.agentHostRun;
	}

	public void setAgentHostRun(java.lang.String value) {
		this.agentHostRun = value;
	}

	public String getLogPath() {
		return this.logPath;
	}

	public void setLogPath(String value) {
		this.logPath = value;
	}

	public java.lang.String getContent() {
		return this.content;
	}

	public void setContent(java.lang.String value) {
		this.content = value;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getTaskId()).append(DateUtils2.dateStr(getTaskDate())).append(getInstanceId()).append(getLogId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof InstanceTaskLog == false)
			return false;
		InstanceTaskLog other = (InstanceTaskLog) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).append(DateUtils2.dateStr(getTaskDate()), DateUtils2.dateStr(other.getTaskDate())).append(getInstanceId(), other.getInstanceId())
				.append(getLogId(), other.getLogId()).isEquals();
	}
}
