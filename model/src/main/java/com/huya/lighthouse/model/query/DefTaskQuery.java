package com.huya.lighthouse.model.query;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.model.query.page.PageQuery;


/**
 */
public class DefTaskQuery extends PageQuery implements Serializable {
    private static final long serialVersionUID = 3148176768559230877L;
    

	/** 任务id */
	private java.lang.Integer taskId;
	/** 所属项目id */
	private java.lang.Integer catalogId;
	private List<Integer> catalogIdList;
	/** 任务名称 */
	private java.lang.String taskName;
	/** 任务类型: 定时器, 虚拟任务... */
	private java.lang.String taskType;
	/** 任务执行插件 */
	private java.lang.String taskPlugin;
	/** 任务内容 */
	private java.lang.String taskBody;
	/** 执行频次quartz表达式 */
	private java.lang.String execCronExp;
	/** 将要在哪个Agent组运行该任务 */
	private java.lang.String agentHostGroup;
	/** linux运行用户 */
	private java.lang.String linuxRunUser;
	/** 所属运行队列 */
	private java.lang.Integer queueId;
	/** 优先级 */
	private java.lang.Integer priority;
	/** 同时运行的最大实例数 */
	private java.lang.Integer maxRunNum;
	/** 运行耗时到达该值，任务就kill掉 */
	private java.lang.Integer maxRunSec;
	/** 任务失败最大重试次数 */
	private java.lang.Integer maxRetryNum;
	/** 重试间隔(秒) */
	private java.lang.Integer retryInterval;
	/** 最终失败是否可忽略, 1可忽略, 0不可忽略 */
	private Integer isIgnoreError;
	/** 是否一次性任务, 1是, 0否 */
	private Integer isOneTimes;
	/** 下线时间 */
	private java.util.Date offlineTimeBegin;
	private java.util.Date offlineTimeEnd;
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

	public java.lang.Integer getTaskId() {
		return this.taskId;
	}
	
	public void setTaskId(java.lang.Integer value) {
		this.taskId = value;
	}
	
	public java.lang.Integer getCatalogId() {
		return this.catalogId;
	}
	
	public void setCatalogId(java.lang.Integer value) {
		this.catalogId = value;
	}
	
	public List<Integer> getCatalogIdList() {
		return catalogIdList;
	}

	public void setCatalogIdList(List<Integer> catalogIdList) {
		this.catalogIdList = catalogIdList;
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
		return this.taskPlugin;
	}
	
	public void setTaskPlugin(java.lang.String value) {
		this.taskPlugin = value;
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
		return this.isOneTimes;
	}
	
	public void setIsOneTimes(Integer value) {
		this.isOneTimes = value;
	}
	
	public java.util.Date getOfflineTimeBegin() {
		return this.offlineTimeBegin;
	}
	
	public void setOfflineTimeBegin(java.util.Date value) {
		this.offlineTimeBegin = value;
	}	
	
	public java.util.Date getOfflineTimeEnd() {
		return this.offlineTimeEnd;
	}
	
	public void setOfflineTimeEnd(java.util.Date value) {
		this.offlineTimeEnd = value;
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

