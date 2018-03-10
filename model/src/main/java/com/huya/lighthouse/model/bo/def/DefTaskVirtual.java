package com.huya.lighthouse.model.bo.def;

import com.huya.lighthouse.model.type.TaskType;

/**
 * 虚拟任务
 *
 */
public class DefTaskVirtual extends AbstractBODefTask {
	
	private static final long serialVersionUID = 2594736657991890675L;

	public DefTaskVirtual() {
		this.setTaskType(TaskType.VIRTUAL.name());
		this.setTaskPlugin("none");
	}

}
