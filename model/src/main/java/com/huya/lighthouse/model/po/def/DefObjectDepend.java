package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.util.AssertUtils;

/**
 * tableName: def_object_depend [DefObjectDepend]
 * 
 */
public class DefObjectDepend implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 1读，0写 db_column: rw_flag
	 */
	private Integer rwFlag;

	/**
	 * 任务id db_column: task_id
	 */
	private java.lang.Integer taskId;

	/**
	 * 对象ID db_column: object_id
	 */
	private java.lang.String objectId;

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

	public DefObjectDepend() {
	}

	public DefObjectDepend(Integer rwFlag, java.lang.Integer taskId, java.lang.String objectId) {
		this.rwFlag = rwFlag;
		this.taskId = taskId;
		this.objectId = objectId;
	}

	public Integer getRwFlag() {
		return this.rwFlag;
	}

	public void setRwFlag(Integer value) {
		this.rwFlag = value;
	}

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}

	public java.lang.String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(java.lang.String value) {
		this.objectId = value;
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
		return new HashCodeBuilder().append(getRwFlag()).append(getTaskId()).append(getObjectId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefObjectDepend == false)
			return false;
		DefObjectDepend other = (DefObjectDepend) obj;
		return new EqualsBuilder().append(getRwFlag(), other.getRwFlag()).append(getTaskId(), other.getTaskId()).append(getObjectId(), other.getObjectId()).isEquals();
	}

	public void doAssert() throws Exception {
		AssertUtils.assertTrue(this.rwFlag != null, "rwFlag cannot be null!");
		AssertUtils.assertTrue(this.taskId != null, "taskId cannot be null!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.objectId), "objectId cannot be blank!");
		AssertUtils.assertTrue(this.isValid != null, "isValid cannot be null!");
	}

	public void doTrim() {
		this.objectId = StringUtils.trim(this.objectId);
	}
}
