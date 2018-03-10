package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * tableName: def_task [DefTask]
 * 
 */
public class DefTask implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 任务id db_column: task_id
	 */
	private java.lang.Integer taskId;

	/**
	 * 所属项目id db_column: catalog_id
	 */
	private java.lang.Integer catalogId;

	/**
	 * 任务名称 db_column: task_name
	 */
	private java.lang.String taskName;

	/**
	 * 任务类型 db_column: task_type
	 */
	private java.lang.String taskType;

	/**
	 * 任务执行插件 db_column: task_plugin
	 */
	private java.lang.String taskPlugin;

	/**
	 * 任务内容 db_column: task_body
	 */
	private java.lang.String taskBody;

	/**
	 * 执行频次quartz表达式 db_column: exec_cron_exp
	 */
	private java.lang.String execCronExp;

	/**
	 * 将要在哪个Agent组运行该任务 db_column: agent_host_group
	 */
	private java.lang.String agentHostGroup;

	/**
	 * linux运行用户 db_column: linux_run_user
	 */
	private java.lang.String linuxRunUser;

	/**
	 * 所属运行队列 db_column: queue_id
	 */
	private java.lang.Integer queueId;

	/**
	 * 优先级 db_column: priority
	 */
	private java.lang.Integer priority;

	/**
	 * 同时运行的最大实例数 db_column: max_run_num
	 */
	private java.lang.Integer maxRunNum;

	/**
	 * 运行耗时到达该值, 任务就kill掉 db_column: max_run_sec
	 */
	private java.lang.Integer maxRunSec;

	/**
	 * 任务失败最大重试次数 db_column: max_retry_num
	 */
	private java.lang.Integer maxRetryNum;

	/**
	 * 重试间隔(秒) db_column: retry_interval
	 */
	private java.lang.Integer retryInterval;

	/**
	 * 最终失败是否可忽略, 1可忽略, 0不可忽略 db_column: is_ignore_error
	 */
	private Integer isIgnoreError;

	/**
	 * 是否一次性任务, 1是, 0否
	 */
	private Integer isOneTimes;

	/**
	 * 下线时间 db_column: offline_time
	 */
	private java.util.Date offlineTime;

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

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}

	public java.lang.Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(java.lang.Integer catalogId) {
		this.catalogId = catalogId;
	}

	public java.lang.String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(java.lang.String value) {
		this.taskName = value;
	}

	public java.lang.String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(java.lang.String value) {
		this.taskType = value;
	}

	public java.lang.String getTaskPlugin() {
		return taskPlugin;
	}

	public void setTaskPlugin(java.lang.String taskPlugin) {
		this.taskPlugin = taskPlugin;
	}

	public java.lang.String getTaskBody() {
		return this.taskBody;
	}

	public void setTaskBody(java.lang.String value) {
		this.taskBody = value;
	}

	public java.lang.String getExecCronExp() {
		return this.execCronExp;
	}

	public void setExecCronExp(java.lang.String value) {
		this.execCronExp = value;
	}

	public java.lang.String getAgentHostGroup() {
		return this.agentHostGroup;
	}

	public void setAgentHostGroup(java.lang.String value) {
		this.agentHostGroup = value;
	}

	public java.lang.String getLinuxRunUser() {
		return this.linuxRunUser;
	}

	public void setLinuxRunUser(java.lang.String value) {
		this.linuxRunUser = value;
	}

	public java.lang.Integer getQueueId() {
		return this.queueId;
	}

	public void setQueueId(java.lang.Integer value) {
		this.queueId = value;
	}

	public java.lang.Integer getPriority() {
		return this.priority;
	}

	public void setPriority(java.lang.Integer value) {
		this.priority = value;
	}

	public java.lang.Integer getMaxRunNum() {
		return this.maxRunNum;
	}

	public void setMaxRunNum(java.lang.Integer value) {
		this.maxRunNum = value;
	}

	public java.lang.Integer getMaxRunSec() {
		return this.maxRunSec;
	}

	public void setMaxRunSec(java.lang.Integer value) {
		this.maxRunSec = value;
	}

	public java.lang.Integer getMaxRetryNum() {
		return this.maxRetryNum;
	}

	public void setMaxRetryNum(java.lang.Integer value) {
		this.maxRetryNum = value;
	}

	public java.lang.Integer getRetryInterval() {
		return this.retryInterval;
	}

	public void setRetryInterval(java.lang.Integer value) {
		this.retryInterval = value;
	}

	public Integer getIsIgnoreError() {
		return this.isIgnoreError;
	}

	public void setIsIgnoreError(Integer value) {
		this.isIgnoreError = value;
	}

	public Integer getIsOneTimes() {
		return isOneTimes;
	}

	public void setIsOneTimes(Integer isOneTimes) {
		this.isOneTimes = isOneTimes;
	}

	public java.util.Date getOfflineTime() {
		return this.offlineTime;
	}

	public void setOfflineTime(java.util.Date value) {
		this.offlineTime = value;
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
		return new HashCodeBuilder().append(getTaskId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefTask == false)
			return false;
		DefTask other = (DefTask) obj;
		return new EqualsBuilder().append(getTaskId(), other.getTaskId()).isEquals();
	}
}
