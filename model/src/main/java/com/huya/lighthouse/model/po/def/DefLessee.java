package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * tableName: def_lessee [DefLessee]
 * 
 */
public class DefLessee implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * id db_column: lessee_id
	 */
	private java.lang.Integer lesseeId;

	/**
	 * 名称 db_column: lessee_name
	 */
	private java.lang.String lesseeName;

	/**
	 * 报警url接口 db_column: alert_url
	 */
	private java.lang.String alertUrl;
	
	private String password;

	private String triggerUrls;
	
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

	public DefLessee() {
	}

	public DefLessee(java.lang.Integer lesseeId) {
		this.lesseeId = lesseeId;
	}

	public java.lang.Integer getLesseeId() {
		return this.lesseeId;
	}

	public void setLesseeId(java.lang.Integer value) {
		this.lesseeId = value;
	}

	public java.lang.String getLesseeName() {
		return this.lesseeName;
	}

	public void setLesseeName(java.lang.String value) {
		this.lesseeName = value;
	}

	public java.lang.String getAlertUrl() {
		return this.alertUrl;
	}

	public void setAlertUrl(java.lang.String value) {
		this.alertUrl = value;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTriggerUrls() {
		return triggerUrls;
	}

	public void setTriggerUrls(String triggerUrls) {
		this.triggerUrls = triggerUrls;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getLesseeId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefLessee == false)
			return false;
		DefLessee other = (DefLessee) obj;
		return new EqualsBuilder().append(getLesseeId(), other.getLesseeId()).isEquals();
	}
}
