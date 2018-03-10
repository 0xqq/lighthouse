package com.huya.lighthouse.model.bo.def;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefClassify;
import com.huya.lighthouse.model.po.def.DefMonitorBegin;
import com.huya.lighthouse.model.po.def.DefMonitorDur;
import com.huya.lighthouse.model.po.def.DefMonitorRetry;
import com.huya.lighthouse.model.po.def.DefObjectDepend;
import com.huya.lighthouse.model.po.def.DefTaskDepend;

public class BODefTaskAllInfo {

	private AbstractBODefTask boDefTask;
	private List<DefTaskDepend> defTaskDependList;
	private List<DefObjectDepend> defObjectDependList;
	private List<DefMonitorBegin> defMonitorBeginList;
	private List<DefMonitorDur> defMonitorDurList;
	private List<DefMonitorRetry> defMonitorRetryList;
	private List<DefClassify> defClassifyList;

	public BODefTaskAllInfo() {
	}

	public BODefTaskAllInfo(AbstractBODefTask boDefTask, List<DefTaskDepend> defTaskDependList, List<DefObjectDepend> defObjectDependList, List<DefMonitorBegin> defMonitorBeginList,
			List<DefMonitorDur> defMonitorDurList, List<DefMonitorRetry> defMonitorRetryList, List<DefClassify> defClassifyList) {
		super();
		this.boDefTask = boDefTask;
		this.defTaskDependList = defTaskDependList;
		this.defObjectDependList = defObjectDependList;
		this.defMonitorBeginList = defMonitorBeginList;
		this.defMonitorDurList = defMonitorDurList;
		this.defMonitorRetryList = defMonitorRetryList;
		this.defClassifyList = defClassifyList;
	}

	public AbstractBODefTask getBoDefTask() {
		return boDefTask;
	}

	public void setBoDefTask(AbstractBODefTask boDefTask) {
		this.boDefTask = boDefTask;
	}

	public List<DefTaskDepend> getDefTaskDependList() {
		return defTaskDependList;
	}

	public void setDefTaskDependList(List<DefTaskDepend> defTaskDependList) {
		this.defTaskDependList = defTaskDependList;
	}

	public List<DefObjectDepend> getDefObjectDependList() {
		return defObjectDependList;
	}

	public void setDefObjectDependList(List<DefObjectDepend> defObjectDependList) {
		this.defObjectDependList = defObjectDependList;
	}

	public List<DefMonitorBegin> getDefMonitorBeginList() {
		return defMonitorBeginList;
	}

	public void setDefMonitorBeginList(List<DefMonitorBegin> defMonitorBeginList) {
		this.defMonitorBeginList = defMonitorBeginList;
	}

	public List<DefMonitorDur> getDefMonitorDurList() {
		return defMonitorDurList;
	}

	public void setDefMonitorDurList(List<DefMonitorDur> defMonitorDurList) {
		this.defMonitorDurList = defMonitorDurList;
	}

	public List<DefMonitorRetry> getDefMonitorRetryList() {
		return defMonitorRetryList;
	}

	public void setDefMonitorRetryList(List<DefMonitorRetry> defMonitorRetryList) {
		this.defMonitorRetryList = defMonitorRetryList;
	}

	public List<DefClassify> getDefClassifyList() {
		return defClassifyList;
	}

	public void setDefClassifyList(List<DefClassify> defClassifyList) {
		this.defClassifyList = defClassifyList;
	}

}
