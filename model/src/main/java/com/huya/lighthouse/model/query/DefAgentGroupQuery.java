package com.huya.lighthouse.model.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.model.query.page.PageQuery;

/**
 * 
 */
public class DefAgentGroupQuery extends PageQuery implements Serializable {
	private static final long serialVersionUID = 3148176768559230877L;

	/** 执行机所属的组 */
	private java.lang.String agentHostGroup;
	/** 执行机域名 */
	private java.lang.String agentHost;
	/** 执行机端口 */
	private java.lang.Integer agentPort;
	/** 执行机用户名 */
	private java.lang.String agentUser;
	/** 执行机私钥路径 */
	private java.lang.String agentPrivateKey;
	/** 工作基础目录，插件和任务日志都在这路径下 */
	private java.lang.String workBaseDir;
	/** 备注 */
	private java.lang.String remarks;
	/** 是否还可用，1可用，0已下线不可用 */
	private Integer isValid;
	/** 创建时间 */
	private java.util.Date createTimeBegin;
	private java.util.Date createTimeEnd;
	/** 更新时间 */
	private java.util.Date updateTimeBegin;
	private java.util.Date updateTimeEnd;
	/** 创建用户 */
	private java.lang.String createUser;
	/** 更新用户 */
	private java.lang.String updateUser;

	public java.lang.String getAgentHostGroup() {
		return this.agentHostGroup;
	}

	public void setAgentHostGroup(java.lang.String value) {
		this.agentHostGroup = value;
	}

	public java.lang.String getAgentHost() {
		return this.agentHost;
	}

	public void setAgentHost(java.lang.String value) {
		this.agentHost = value;
	}

	public java.lang.Integer getAgentPort() {
		return this.agentPort;
	}

	public void setAgentPort(java.lang.Integer value) {
		this.agentPort = value;
	}

	public java.lang.String getAgentUser() {
		return this.agentUser;
	}

	public void setAgentUser(java.lang.String value) {
		this.agentUser = value;
	}

	public java.lang.String getAgentPrivateKey() {
		return this.agentPrivateKey;
	}

	public void setAgentPrivateKey(java.lang.String value) {
		this.agentPrivateKey = value;
	}

	public java.lang.String getWorkBaseDir() {
		return this.workBaseDir;
	}

	public void setWorkBaseDir(java.lang.String value) {
		this.workBaseDir = value;
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

	public java.util.Date getCreateTimeBegin() {
		return this.createTimeBegin;
	}

	public void setCreateTimeBegin(java.util.Date value) {
		this.createTimeBegin = value;
	}

	public java.util.Date getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setCreateTimeEnd(java.util.Date value) {
		this.createTimeEnd = value;
	}

	public java.util.Date getUpdateTimeBegin() {
		return this.updateTimeBegin;
	}

	public void setUpdateTimeBegin(java.util.Date value) {
		this.updateTimeBegin = value;
	}

	public java.util.Date getUpdateTimeEnd() {
		return this.updateTimeEnd;
	}

	public void setUpdateTimeEnd(java.util.Date value) {
		this.updateTimeEnd = value;
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

}
