package com.huya.lighthouse.model.bo.instance;

import java.util.ArrayList;
import java.util.List;

import com.huya.lighthouse.model.bo.def.AbstractBODefTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;

public class BOInstanceTask {

	private AbstractBODefTask boDefTask;
	private List<InstanceTaskDepend> preDepends = new ArrayList<InstanceTaskDepend>();
	private List<InstanceTaskDepend> postDepends = new ArrayList<InstanceTaskDepend>();

	/**
	 * 任务数据日期 db_column: task_date
	 */
	private java.util.Date taskDate;

	/**
	 * 实例id db_column: instance_id
	 */
	private java.lang.String instanceId;

	/**
	 * 实际在哪个Agent运行该任务 db_column: agent_host_run
	 */
	private java.lang.String agentHostRun;

	/**
	 * 在哪些Agent运行过该任务，用分号分隔 db_column: agent_host_run_his
	 */
	private java.lang.String agentHostRunHis;

	/**
	 * 已经重试的次数 db_column: retried_num
	 */
	private java.lang.Integer retriedNum = 0;

	/**
	 * 准备好要运行的时间 db_column: ready_time
	 */
	private java.util.Date readyTime;

	/**
	 * 开始时间 db_column: start_time
	 */
	private java.util.Date startTime;

	/**
	 * 结束时间 db_column: end_time
	 */
	private java.util.Date endTime;

	/**
	 * 状态 db_column: status
	 */
	private java.lang.String status;

	/**
	 * 是否只运行自己,不触发后续依赖, 1是, 0否db_column: is_self_run
	 */
	private Integer isSelfRun;

	/**
	 * 是否强制运行, 1是, 0否, 1是, 0否db_column: is_force_run
	 */
	private Integer isForceRun;

	public AbstractBODefTask getBoDefTask() {
		return boDefTask;
	}

	public void setBoDefTask(AbstractBODefTask boDefTask) {
		this.boDefTask = boDefTask;
	}

	public java.util.Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(java.util.Date taskDate) {
		this.taskDate = taskDate;
	}

	public java.lang.String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(java.lang.String instanceId) {
		this.instanceId = instanceId;
	}

	public java.lang.String getAgentHostRun() {
		return agentHostRun;
	}

	public void setAgentHostRun(java.lang.String agentHostRun) {
		this.agentHostRun = agentHostRun;
	}

	public java.lang.String getAgentHostRunHis() {
		return agentHostRunHis;
	}

	public void setAgentHostRunHis(java.lang.String agentHostRunHis) {
		this.agentHostRunHis = agentHostRunHis;
	}

	public java.lang.Integer getRetriedNum() {
		return retriedNum;
	}

	public void setRetriedNum(java.lang.Integer retriedNum) {
		this.retriedNum = retriedNum;
	}

	public java.util.Date getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(java.util.Date readyTime) {
		this.readyTime = readyTime;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public Integer getIsSelfRun() {
		return isSelfRun;
	}

	public void setIsSelfRun(Integer isSelfRun) {
		this.isSelfRun = isSelfRun;
	}

	public Integer getIsForceRun() {
		return isForceRun;
	}

	public void setIsForceRun(Integer isForceRun) {
		this.isForceRun = isForceRun;
	}

	public List<InstanceTaskDepend> getPreDepends() {
		return preDepends;
	}

	public void setPreDepends(List<InstanceTaskDepend> preDepends) {
		this.preDepends = preDepends;
	}

	public List<InstanceTaskDepend> getPostDepends() {
		return postDepends;
	}

	public void setPostDepends(List<InstanceTaskDepend> postDepends) {
		this.postDepends = postDepends;
	}

}
