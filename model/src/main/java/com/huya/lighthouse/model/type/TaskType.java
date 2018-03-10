package com.huya.lighthouse.model.type;

import java.util.Arrays;
import java.util.List;

/**
 * 任务类型
 */
public enum TaskType {

	VIRTUAL("VIRTUAL", "虚拟任务"), CRON("CRON", "定时任务"), SHELL("SHELL", "shell任务"), HQL("HQL", "hive sql任务"), TOJDBC("TOJDBC", "导出至jdbc任务"), TOREDIS("TOREDIS", "导出至redis任务"), TORSYNC("TORSYNC",
			"rsync推送任务"), HTTP("HTTP", "http任务"), JDBC2HIVE("JDBC2HIVE", "jdbc导数据至hive任务"), RSYNC2HIVE("RSYNC2HIVE", "rsync拉数据至hive任务"), DATAPATHCHECK(
			"DATAPATHCHECK", "数据检查任务");

	private String taskType;
	private String taskTypeName;

	private TaskType(String taskType, String taskTypeName) {
		this.taskType = taskType;
		this.taskTypeName = taskTypeName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public static TaskType getTaskTypeEnum(String taskType) {
		return Enum.valueOf(TaskType.class, taskType);
	}

	public static List<TaskType> getAll() {
		return Arrays.asList(VIRTUAL, CRON, SHELL, HQL, TOJDBC, TOREDIS, TORSYNC, HTTP, JDBC2HIVE, RSYNC2HIVE, DATAPATHCHECK);
	}
}
