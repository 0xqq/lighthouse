package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * tableName: def_task_param [DefTaskParam]
 * 
 */
public class DefTaskParam implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 所属项目id db_column: catalog_id
	 */
	private java.lang.Integer catalogId;

	/**
	 * 参数名 db_column: param_code
	 */
	private java.lang.String paramCode;

	/**
	 * 参数名 db_column: param_type
	 */
	private java.lang.String paramType;

	/**
	 * 参数值 db_column: param_value
	 */
	private java.lang.String paramValue;

	/**
	 * 是否为密码类型，1是，0否 db_column: is_password
	 */
	private Integer isPassword;

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

	public DefTaskParam() {
	}

	public DefTaskParam(java.lang.String paramCode) {
		this.paramCode = paramCode;
	}

	public java.lang.String getParamCode() {
		return this.paramCode;
	}

	public void setParamCode(java.lang.String value) {
		this.paramCode = value;
	}

	public java.lang.String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(java.lang.String value) {
		this.paramValue = value;
	}

	public java.lang.String getParamType() {
		return paramType;
	}

	public void setParamType(java.lang.String paramType) {
		this.paramType = paramType;
	}

	public java.lang.Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(java.lang.Integer catalogId) {
		this.catalogId = catalogId;
	}

	public Integer getIsPassword() {
		return isPassword;
	}

	public void setIsPassword(Integer isPassword) {
		this.isPassword = isPassword;
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
		return new HashCodeBuilder().append(getCatalogId()).append(getParamCode()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefTaskParam == false)
			return false;
		DefTaskParam other = (DefTaskParam) obj;
		return new EqualsBuilder().append(getCatalogId(), other.getCatalogId()).append(getParamCode(), other.getParamCode()).isEquals();
	}
}
