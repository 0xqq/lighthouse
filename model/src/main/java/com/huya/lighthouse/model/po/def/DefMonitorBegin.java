package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.quartz.CronExpression;

import com.huya.lighthouse.util.AssertUtils;

/**
 * tableName: def_monitor_begin [DefMonitorBegin]
 * 
 */
public class DefMonitorBegin implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 定时器 db_column: cronExp
	 */
	private java.lang.String cronExp;

	private java.lang.Integer moveMinute = 0;

	private java.lang.Integer moveHour = 0;

	private java.lang.Integer moveDay = 0;

	private java.lang.Integer moveMonth = 0;
	
	/**
	 * 任务id db_column: task_id
	 */
	private java.lang.Integer taskId;

	/**
	 * 备注 db_column: remarks
	 */
	private java.lang.String remarks;

	/**
	 * 是否还可用，1可用，0已下线不可用 db_column: is_valid
	 */
	private Integer isValid;

	/**
	 * 创建时间 db_column: create_time
	 */
	private java.util.Date createTime;

	/**
	 * 更新时间 db_column: update_time
	 */
	private java.util.Date updateTime;

	/**
	 * 创建用户 db_column: create_user
	 */
	private java.lang.String createUser;

	/**
	 * 更新用户 db_column: update_user
	 */
	private java.lang.String updateUser;


	public DefMonitorBegin() {
	}

	public DefMonitorBegin(String cronExp, Integer taskId) {
		super();
		this.cronExp = cronExp;
		this.taskId = taskId;
	}

	public java.lang.String getCronExp() {
		return cronExp;
	}

	public void setCronExp(java.lang.String cronExp) {
		this.cronExp = cronExp;
	}

	public int getMoveMinute() {
		return moveMinute;
	}

	public void setMoveMinute(int moveMinute) {
		this.moveMinute = moveMinute;
	}

	public int getMoveHour() {
		return moveHour;
	}

	public void setMoveHour(int moveHour) {
		this.moveHour = moveHour;
	}

	public int getMoveDay() {
		return moveDay;
	}

	public void setMoveDay(int moveDay) {
		this.moveDay = moveDay;
	}

	public int getMoveMonth() {
		return moveMonth;
	}

	public void setMoveMonth(int moveMonth) {
		this.moveMonth = moveMonth;
	}

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
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

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}

	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
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

	public int hashCode() {
		return new HashCodeBuilder().append(getCronExp()).append(getTaskId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefMonitorBegin == false)
			return false;
		DefMonitorBegin other = (DefMonitorBegin) obj;
		return new EqualsBuilder().append(getCronExp(), other.getCronExp()).append(getTaskId(), other.getTaskId()).isEquals();
	}

	public void doAssert() throws Exception {
		new CronExpression(this.cronExp);
		AssertUtils.assertTrue(this.taskId != null, "taskId cannot be null!");
		AssertUtils.assertTrue(this.isValid != null, "isValid cannot be null!");
	}

	public void doTrim() {
	}
}
