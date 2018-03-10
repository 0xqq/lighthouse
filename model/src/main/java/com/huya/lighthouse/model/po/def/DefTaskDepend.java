package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.huya.lighthouse.model.type.DependType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * tableName: def_task_depend [DefTaskDepend]
 * 
 */
public class DefTaskDepend implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 任务id db_column: task_id
	 */
	private java.lang.Integer taskId;

	/**
	 * 前置依赖的任务id db_column: pre_task_id
	 */
	private java.lang.Integer preTaskId;

	/**
	 * 依赖类型 db_column: depend_type
	 */
	private java.lang.String dependType;

	/**
	 * 依赖个数 db_column: depend_n
	 */
	private java.lang.Integer dependN = 1;

	/**
	 * 是否有状态任务，需要在同个agent节点运行, 1是, 0否 db_column: is_same_agent_run
	 */
	private java.lang.Integer isSameAgentRun = 0;

	/**
	 * 是否依赖上下文, 1是, 0否
	 */
	private java.lang.Integer isContextDepend = 0;

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

	public DefTaskDepend() {
	}

	public DefTaskDepend(Integer taskId, Integer preTaskId, String dependType, Integer dependN, Integer isSameAgentRun, Integer isContextDepend) {
		this.taskId = taskId;
		this.preTaskId = preTaskId;
		this.dependType = dependType;
		this.dependN = dependN;
		this.isSameAgentRun = isSameAgentRun;
		this.isContextDepend = isContextDepend;
	}

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}

	public java.lang.Integer getPreTaskId() {
		return this.preTaskId;
	}

	public void setPreTaskId(java.lang.Integer value) {
		this.preTaskId = value;
	}

	public java.lang.String getDependType() {
		return this.dependType;
	}

	public void setDependType(java.lang.String value) {
		this.dependType = value;
	}

	public java.lang.Integer getDependN() {
		return this.dependN;
	}

	public void setDependN(java.lang.Integer value) {
		this.dependN = value;
	}

	public java.lang.Integer getIsSameAgentRun() {
		return isSameAgentRun;
	}

	public void setIsSameAgentRun(java.lang.Integer isSameAgentRun) {
		this.isSameAgentRun = isSameAgentRun;
	}

	public java.lang.Integer getIsContextDepend() {
		return isContextDepend;
	}

	public void setIsContextDepend(java.lang.Integer isContextDepend) {
		this.isContextDepend = isContextDepend;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getTaskId()).append(getPreTaskId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefTaskDepend == false)
			return false;
		DefTaskDepend other = (DefTaskDepend) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).append(getPreTaskId(), other.getPreTaskId()).isEquals();
	}

	public void doAssert() throws Exception {
		AssertUtils.assertTrue(this.taskId != null, "taskId cannot be null!");
		AssertUtils.assertTrue(this.preTaskId != null, "preTaskId cannot be null!");
		Enum.valueOf(DependType.class, this.dependType);
		AssertUtils.assertTrue(this.dependN != null, "dependN cannot be null!");
		AssertUtils.assertTrue(this.isSameAgentRun != null, "isSameAgentRun cannot be null!");
		AssertUtils.assertTrue(this.isValid != null, "isValid cannot be null!");
	}

}
