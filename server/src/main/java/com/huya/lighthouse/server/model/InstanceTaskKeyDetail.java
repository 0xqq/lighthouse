package com.huya.lighthouse.server.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.huya.lighthouse.util.DateUtils2;

/**
 * 
 */
public class InstanceTaskKeyDetail implements Comparable<InstanceTaskKeyDetail> {

	private Integer taskId;
	private Date taskDate;
	private String instanceId;

	public InstanceTaskKeyDetail() {
		super();
	}

	public InstanceTaskKeyDetail(Integer taskId, Date taskDate, String instanceId) {
		super();
		this.taskId = taskId;
		this.taskDate = taskDate;
		this.instanceId = instanceId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public InstanceTaskKey getInstanceTaskKey() {
		return new InstanceTaskKey(taskId, taskDate);
	}

	@Override
	public int compareTo(InstanceTaskKeyDetail o) {
		if (o == null) {
			return -1;
		}
		if (this.equals(o)) {
			return 0;
		}
		return -1;
	}

	@Override
	public String toString() {
		return String.format("taskId=%s, taskDate=%s, instanceId=%s", taskId, DateUtils2.dateStr(taskDate), instanceId);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getTaskId()).append(DateUtils2.dateStr(getTaskDate())).append(getInstanceId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof InstanceTaskKeyDetail == false) {
			return false;
		}
		InstanceTaskKeyDetail other = (InstanceTaskKeyDetail) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).append(DateUtils2.dateStr(getTaskDate()), DateUtils2.dateStr(other.getTaskDate())).append(getInstanceId(), other.getInstanceId()).isEquals();
	}
}
