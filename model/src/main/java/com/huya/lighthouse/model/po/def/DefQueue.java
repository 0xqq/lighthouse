package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * tableName: def_queue [DefQueue]
 * 
 */
public class DefQueue implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 队列id db_column: queue_id
	 */
	private java.lang.Integer queueId;

	/**
	 * 队列名称 db_column: queue_name
	 */
	private java.lang.String queueName;

	/**
	 * 队列允许最大同时执行数 db_column: queue_size
	 */
	private java.lang.Integer queueSize;

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

	public DefQueue() {
	}

	public DefQueue(java.lang.Integer queueId) {
		this.queueId = queueId;
	}

	public java.lang.Integer getQueueId() {
		return this.queueId;
	}

	public void setQueueId(java.lang.Integer value) {
		this.queueId = value;
	}

	public java.lang.String getQueueName() {
		return this.queueName;
	}

	public void setQueueName(java.lang.String value) {
		this.queueName = value;
	}

	public java.lang.Integer getQueueSize() {
		return this.queueSize;
	}

	public void setQueueSize(java.lang.Integer value) {
		this.queueSize = value;
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
		return new HashCodeBuilder().append(getQueueId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefQueue == false)
			return false;
		DefQueue other = (DefQueue) obj;
		return new EqualsBuilder().append(getQueueId(), other.getQueueId()).isEquals();
	}
}
