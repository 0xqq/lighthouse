package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * tableName: def_agent_group [DefAgentGroup]
 * 
 */
public class DefAgentGroup implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 执行机所属的组 db_column: agent_host_group
	 */
	private java.lang.String agentHostGroup;

	/**
	 * 执行机域名 db_column: agent_host
	 */
	private java.lang.String agentHost;

	/**
	 * 执行机端口 db_column: agent_port
	 */
	private java.lang.Integer agentPort;

	/**
	 * 执行机用户名 db_column: agent_user
	 */
	private java.lang.String agentUser;

	/**
	 * 执行机私钥路径 db_column: agent_private_key
	 */
	private java.lang.String agentPrivateKey;
	
	private String agentPassword;

	/**
	 * 工作基础目录，插件和任务日志都在这路径下 db_column: work_base_dir
	 */
	private java.lang.String workBaseDir;

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

	public DefAgentGroup() {
	}

	public DefAgentGroup(java.lang.String agentHostGroup, java.lang.String agentHost) {
		this.agentHostGroup = agentHostGroup;
		this.agentHost = agentHost;
	}

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
		return agentPort;
	}

	public void setAgentPort(java.lang.Integer agentPort) {
		this.agentPort = agentPort;
	}

	public java.lang.String getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(java.lang.String agentUser) {
		this.agentUser = agentUser;
	}

	public java.lang.String getAgentPrivateKey() {
		return agentPrivateKey;
	}

	public void setAgentPrivateKey(java.lang.String agentPrivateKey) {
		this.agentPrivateKey = agentPrivateKey;
	}

	public String getAgentPassword() {
		return agentPassword;
	}

	public void setAgentPassword(String agentPassword) {
		this.agentPassword = agentPassword;
	}

	public java.lang.String getWorkBaseDir() {
		return workBaseDir;
	}

	public void setWorkBaseDir(java.lang.String workBaseDir) {
		this.workBaseDir = workBaseDir;
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
		return new HashCodeBuilder().append(getAgentHostGroup()).append(getAgentHost()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefAgentGroup == false)
			return false;
		DefAgentGroup other = (DefAgentGroup) obj;
		return new EqualsBuilder().append(getAgentHostGroup(), other.getAgentHostGroup()).append(getAgentHost(), other.getAgentHost()).isEquals();
	}
}
