package com.huya.lighthouse.model.bo.misc;

import org.apache.commons.lang.builder.ToStringBuilder;

public class StatusCnt {

	private Integer allCnt;
	private Integer initCnt;
	private Integer readyCnt;
	private Integer runningCnt;
	private Integer successCnt;
	private Integer failCnt;
	private Integer killCnt;

	public Integer getAllCnt() {
		return allCnt;
	}

	public void setAllCnt(Integer allCnt) {
		this.allCnt = allCnt;
	}

	public Integer getInitCnt() {
		return initCnt;
	}

	public void setInitCnt(Integer initCnt) {
		this.initCnt = initCnt;
	}

	public Integer getReadyCnt() {
		return readyCnt;
	}

	public void setReadyCnt(Integer readyCnt) {
		this.readyCnt = readyCnt;
	}

	public Integer getRunningCnt() {
		return runningCnt;
	}

	public void setRunningCnt(Integer runningCnt) {
		this.runningCnt = runningCnt;
	}

	public Integer getSuccessCnt() {
		return successCnt;
	}

	public void setSuccessCnt(Integer successCnt) {
		this.successCnt = successCnt;
	}

	public Integer getFailCnt() {
		return failCnt;
	}

	public void setFailCnt(Integer failCnt) {
		this.failCnt = failCnt;
	}

	public Integer getKillCnt() {
		return killCnt;
	}

	public void setKillCnt(Integer killCnt) {
		this.killCnt = killCnt;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
