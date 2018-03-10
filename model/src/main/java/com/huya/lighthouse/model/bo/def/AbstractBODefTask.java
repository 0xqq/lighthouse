package com.huya.lighthouse.model.bo.def;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.quartz.CronExpression;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 业务任务类型基础类
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "CLASS")
public abstract class AbstractBODefTask implements java.io.Serializable {

	private static final long serialVersionUID = 5198698762506679634L;

	/**
	 * 任务id db_column: task_id
	 */
	private java.lang.Integer taskId;

	/**
	 * 所属项目id db_column: project_id
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

	public void doAssert() throws Exception {
		AssertUtils.assertTrue(this.catalogId != null, "catalogId cannot be null!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.taskName), "taskName cannot be blank!");
		Enum.valueOf(TaskType.class, this.taskType);
		new CronExpression(this.execCronExp);
		AssertUtils.assertTrue(StringUtils.startsWith(this.execCronExp, "0 "), "execCronExp second must be 0!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.taskPlugin), "taskPlugin cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.agentHostGroup), "agentHostGroup cannot be blank!");
		AssertUtils.assertTrue(this.queueId != null, "queueId cannot be null!");
		AssertUtils.assertTrue(this.priority != null, "priority cannot be null!");
		AssertUtils.assertTrue(this.maxRunNum != null, "maxRunNum cannot be null!");
		AssertUtils.assertTrue(this.maxRunSec != null, "maxRunSec cannot be null!");
		AssertUtils.assertTrue(this.maxRetryNum != null, "maxRetryNum cannot be null!");
		AssertUtils.assertTrue(this.retryInterval != null, "retryInterval cannot be null!");
		AssertUtils.assertTrue(this.isIgnoreError != null, "isIgnoreError cannot be null!");
		AssertUtils.assertTrue(this.offlineTime != null, "offlineTime cannot be null!");
		AssertUtils.assertTrue(this.isValid != null, "isValid cannot be null!");
		AssertUtils.assertTrue(this.createTime != null, "createTime cannot be null!");
		AssertUtils.assertTrue(this.updateTime != null, "updateTime cannot be null!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.createUser), "createUser cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.updateUser), "updateUser cannot be blank!");
	}

	public void doTrim() {
		this.taskName = StringUtils.trim(this.taskName);
		this.taskPlugin = StringUtils.trim(this.taskPlugin);
		this.agentHostGroup = StringUtils.trim(this.agentHostGroup);
		this.linuxRunUser = StringUtils.trim(this.linuxRunUser);
		this.remarks = StringUtils.trim(this.remarks);
		this.createUser = StringUtils.trim(this.createUser);
		this.updateUser = StringUtils.trim(this.updateUser);
	}

	public void registerConverter() {
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(@SuppressWarnings("rawtypes") Class type, Object value) {
				if (value == null) {
					return null;
				}
				return value;
			}
		}, Date.class);
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
