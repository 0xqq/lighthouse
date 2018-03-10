package com.huya.lighthouse.model.po.instance;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.util.DateUtils2;

public class InstanceTaskKey {

	private java.lang.Integer taskId;

	private java.util.Date taskDate;

	private java.lang.String instanceId;

	public InstanceTaskKey() {
	}

	public InstanceTaskKey(Integer taskId, Date taskDate, String instanceId) {
		super();
		this.taskId = taskId;
		this.taskDate = taskDate;
		this.instanceId = instanceId;
	}

	public java.lang.Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(java.lang.Integer taskId) {
		this.taskId = taskId;
	}

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
