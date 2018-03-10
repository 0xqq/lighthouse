package com.huya.lighthouse.model.query.misc;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.query.page.PageQuery;

public class StatusCntCatalogDayQuery extends PageQuery {

	private static final long serialVersionUID = 9199441003585880544L;
	private List<Integer> catalogIds; //为空，表示查所有项目
	private Date taskDateBegin;
	private Date taskDateEnd;
	
	public List<Integer> getCatalogIds() {
		return catalogIds;
	}
	public void setCatalogIds(List<Integer> catalogIds) {
		this.catalogIds = catalogIds;
	}
	public Date getTaskDateBegin() {
		return taskDateBegin;
	}
	public void setTaskDateBegin(Date taskDateBegin) {
		this.taskDateBegin = taskDateBegin;
	}
	public Date getTaskDateEnd() {
		return taskDateEnd;
	}
	public void setTaskDateEnd(Date taskDateEnd) {
		this.taskDateEnd = taskDateEnd;
	}
	
	
}
