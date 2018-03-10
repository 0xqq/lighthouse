package com.huya.lighthouse.model.type;

import java.util.Arrays;
import java.util.List;

/**
 * 依赖类型的种类
 * 
 */
public enum DependType {
	SAMELEVEL("SAMELEVEL", "同级依赖"), SELF("SELF", "自我依赖"), SLIDE("SLIDE", "滑动依赖"), HOUR("HOUR", "跨度一小时"), DAY("DAY", "跨度一天"), WEEK("WEEK", "跨度一周"), MONTH("MONTH", "跨度一个月"), QUARTER("QUARTER", "跨度一个季度");

	private String dependType;
	private String dependTypeName;

	private DependType(String dependType, String dependTypeName) {
		this.dependType = dependType;
		this.dependTypeName = dependTypeName;
	}

	public String getDependType() {
		return dependType;
	}

	public void setDependType(String dependType) {
		this.dependType = dependType;
	}

	public String getDependTypeName() {
		return dependTypeName;
	}

	public void setDependTypeName(String dependTypeName) {
		this.dependTypeName = dependTypeName;
	}

	public static DependType getDependTypeEnum(String dependType) {
		return Enum.valueOf(DependType.class, dependType);
	}

	public static List<DependType> getAll() {
		return Arrays.asList(SAMELEVEL, SELF, SLIDE, HOUR, DAY, WEEK, MONTH, QUARTER);
	}
}
