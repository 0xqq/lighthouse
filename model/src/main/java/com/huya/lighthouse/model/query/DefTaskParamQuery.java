package com.huya.lighthouse.model.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.huya.lighthouse.model.query.page.PageQuery;

/**
 * 
 */
public class DefTaskParamQuery extends PageQuery implements Serializable {
	private static final long serialVersionUID = 3148176768559230877L;

	/** 所属项目id */
	private java.lang.Integer catalogId;
	/** 参数名 */
	private java.lang.String paramCode;
	/** 参数类型 */
	private java.lang.String paramType;
	/** 参数值 */
	private java.lang.String paramValue;
	/** 是否为密码类型，1是，0否 */
	private Integer isPassword;
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

	public java.lang.Integer getCatalogId() {
		return this.catalogId;
	}

	public void setCatalogId(java.lang.Integer value) {
		this.catalogId = value;
	}

	public java.lang.String getParamCode() {
		return this.paramCode;
	}

	public void setParamCode(java.lang.String value) {
		this.paramCode = value;
	}

	public java.lang.String getParamType() {
		return this.paramType;
	}

	public void setParamType(java.lang.String value) {
		this.paramType = value;
	}

	public java.lang.String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(java.lang.String value) {
		this.paramValue = value;
	}

	public Integer getIsPassword() {
		return this.isPassword;
	}

	public void setIsPassword(Integer value) {
		this.isPassword = value;
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
