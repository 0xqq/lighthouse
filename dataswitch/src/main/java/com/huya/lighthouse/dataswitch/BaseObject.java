package com.huya.lighthouse.dataswitch;

import java.util.Properties;

public class BaseObject {

	private String id; // 对象ID
	private String remarks; // 对象备注
	private Properties props; // 对象自定义属性

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
