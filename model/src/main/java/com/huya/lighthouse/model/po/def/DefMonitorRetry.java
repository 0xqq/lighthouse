package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.util.AssertUtils;

/**
 * tableName: def_monitor_retry [DefMonitorRetry]
 * 
 */
public class DefMonitorRetry implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 任务id db_column: task_id
	 */
	private java.lang.Integer taskId;

	/**
	 * 失败重试多少次，就报警 db_column: fail_retry_n
	 */
	private java.lang.Integer failRetryN;

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

	public DefMonitorRetry() {
	}

	public DefMonitorRetry(Integer taskId, Integer failRetryN) {
		super();
		this.taskId = taskId;
		this.failRetryN = failRetryN;
	}

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}

	public java.lang.Integer getFailRetryN() {
		return this.failRetryN;
	}

	public void setFailRetryN(java.lang.Integer value) {
		this.failRetryN = value;
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
		return new HashCodeBuilder().append(getTaskId()).append(getFailRetryN()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefMonitorRetry == false)
			return false;
		DefMonitorRetry other = (DefMonitorRetry) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).append(getFailRetryN(), other.getFailRetryN()).isEquals();
	}

	public void doAssert() throws Exception {
		AssertUtils.assertTrue(this.taskId != null, "taskId cannot be null!");
		AssertUtils.assertTrue(this.failRetryN != null, "failRetryN cannot be null!");
		AssertUtils.assertTrue(this.isValid != null, "isValid cannot be null!");
	}

	public void doTrim() {
	}
}
