package com.huya.lighthouse.model.po.def;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * tableName: def_catalog [DefCatalog]
 * 
 */
public class DefCatalog implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * id db_column: catalog_id
	 */
	private java.lang.Integer catalogId;

	/**
	 * 名称 db_column: catalog_name
	 */
	private java.lang.String catalogName;

	/**
	 * 租户id db_column: lessee_id
	 */
	private java.lang.Integer lesseeId = 10000;

	/**
	 * 备注 db_column: remarks
	 */
	private java.lang.String remarks;

	/**
	 * 是否还可用，1可用，0已下线不可用 db_column: is_valid
	 */
	private Integer isValid = 1;

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

	public DefCatalog() {
	}

	public DefCatalog(java.lang.Integer catalogId) {
		this.catalogId = catalogId;
	}

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
		return new HashCodeBuilder().append(getCatalogId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof DefCatalog == false)
			return false;
		DefCatalog other = (DefCatalog) obj;
		return new EqualsBuilder().append(getCatalogId(), other.getCatalogId()).isEquals();
	}
}
