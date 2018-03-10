package com.huya.lighthouse.model.po.instance;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.huya.lighthouse.model.po.def.DefTaskDepend;
import com.huya.lighthouse.util.DateUtils2;

/**
 * tableName: instance_task_depend [InstanceTaskDepend]
 * 
 */
public class InstanceTaskDepend extends DefTaskDepend {

	private static final long serialVersionUID = 5454155825314635342L;

	private java.util.Date taskDate;

	private java.lang.String instanceId;

	private java.util.Date preTaskDate;

	public InstanceTaskDepend() {
	}

	public InstanceTaskDepend(java.lang.Integer taskId, java.util.Date taskDate, String instanceId, java.lang.Integer preTaskId, java.util.Date preTaskDate) {
		setTaskId(taskId);
		this.taskDate = taskDate;
		this.instanceId = instanceId;
		setPreTaskId(preTaskId);
		this.preTaskDate = preTaskDate;
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

	public java.util.Date getPreTaskDate() {
		return preTaskDate;
	}

	public void setPreTaskDate(java.util.Date preTaskDate) {
		this.preTaskDate = preTaskDate;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getTaskId()).append(DateUtils2.dateStr(getTaskDate())).append(getInstanceId()).append(getPreTaskId()).append(DateUtils2.dateStr(getPreTaskDate())).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof InstanceTaskDepend == false)
			return false;
		InstanceTaskDepend other = (InstanceTaskDepend) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).append(DateUtils2.dateStr(getTaskDate()), DateUtils2.dateStr(other.getTaskDate())).append(getInstanceId(), other.getInstanceId())
				.append(getPreTaskId(), other.getPreTaskId()).append(DateUtils2.dateStr(getPreTaskDate()), DateUtils2.dateStr(other.getPreTaskDate())).isEquals();
	}
}
