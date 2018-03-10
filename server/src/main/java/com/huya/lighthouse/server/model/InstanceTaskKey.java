package com.huya.lighthouse.server.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.huya.lighthouse.util.DateUtils2;

/**
 * 以taskId和taskDate，确定一个任务实例
 * 
 */
public class InstanceTaskKey implements Comparable<InstanceTaskKey> {

	private Integer taskId;
	private Date taskDate;

	public InstanceTaskKey() {
		super();
	}

	public InstanceTaskKey(Integer taskId, Date taskDate) {
		super();
		this.taskId = taskId;
		this.taskDate = taskDate;
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

	@Override
	public int compareTo(InstanceTaskKey o) {
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
		return String.format("taskId=%s, taskDate=%s", taskId, DateUtils2.dateStr(taskDate));
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getTaskId()).append(DateUtils2.dateStr(getTaskDate())).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof InstanceTaskKey == false) {
			return false;
		}
		InstanceTaskKey other = (InstanceTaskKey) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).append(DateUtils2.dateStr(getTaskDate()), DateUtils2.dateStr(other.getTaskDate())).isEquals();
	}
}
