package com.huya.lighthouse.model.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.model.query.page.PageQuery;

/**
 * 
 */
public class DefCatalogQuery extends PageQuery implements Serializable {
	private static final long serialVersionUID = 3148176768559230877L;

	/** id */
	private java.lang.Integer catalogId;
	/** 名称 */
	private java.lang.String catalogName;
	/** 租户id */
	private java.lang.Integer lesseeId;
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

	public java.lang.Integer getCatalogId() {
		return this.catalogId;
	}

	public void setCatalogId(java.lang.Integer value) {
		this.catalogId = value;
	}

	public java.lang.String getCatalogName() {
		return this.catalogName;
	}

	public void setCatalogName(java.lang.String value) {
		this.catalogName = value;
	}

	public java.lang.Integer getLesseeId() {
		return this.lesseeId;
	}

	public void setLesseeId(java.lang.Integer value) {
		this.lesseeId = value;
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
