package com.huya.lighthouse.model.query.misc;

import com.huya.lighthouse.model.query.page.PageQuery;

public class StatusCntQuery extends PageQuery implements java.io.Serializable {

	private static final long serialVersionUID = 718106215256744392L;
	
	private Integer catalogId; //为空，表示查所有catalog
	private String status; //为空，表示查所有状态
	private String taskBody; //模糊查询，可以是任务ID，或任务名称，或任务插件任意属性

	
	public Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskBody() {
		return taskBody;
	}

	public void setTaskBody(String taskBody) {
		this.taskBody = taskBody;
	}
	
}
