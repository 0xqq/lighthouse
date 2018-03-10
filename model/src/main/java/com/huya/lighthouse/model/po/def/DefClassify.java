package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.util.AssertUtils;

/**
 * tableName: def_classify [DefClassify]
 * 
 */
public class DefClassify implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	/**
	 * 分类类别 db_column: classify_type
	 */
	private java.lang.String classifyType;

	/**
	 * 分类代码 db_column: classify_code
	 */
	private java.lang.String classifyCode;

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
	private Integer isValid = 1;

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

	public DefClassify() {
	}

	public DefClassify(java.lang.String classifyType, java.lang.String classifyCode, java.lang.Integer taskId) {
		this.classifyType = classifyType;
		this.classifyCode = classifyCode;
		this.taskId = taskId;
	}

	public java.lang.String getClassifyType() {
		return this.classifyType;
	}

	public void setClassifyType(java.lang.String value) {
		this.classifyType = value;
	}

	public java.lang.String getClassifyCode() {
		return this.classifyCode;
	}

	public void setClassifyCode(java.lang.String value) {
		this.classifyCode = value;
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
		return new HashCodeBuilder().append(getClassifyType()).append(getClassifyCode()).append(getTaskId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefClassify == false)
			return false;
		DefClassify other = (DefClassify) obj;
		return new EqualsBuilder().append(getClassifyType(), other.getClassifyType()).append(getClassifyCode(), other.getClassifyCode()).append(getTaskId(), other.getTaskId()).isEquals();
	}

	public void doAssert() throws Exception {
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.classifyType), "classifyType cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.classifyCode), "classifyCode cannot be blank!");
		AssertUtils.assertTrue(this.taskId != null, "taskId cannot be null!");
	}

	public void doTrim() {
		this.classifyType = StringUtils.trim(this.classifyType);
		this.classifyCode = StringUtils.trim(this.classifyCode);
	}
}
