package com.huya.lighthouse.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class FileSizeUtils {
	public static long ONE_KB = 1024L;
	public static long ONE_MB = ONE_KB * 1024L;
	public static long ONE_GB = ONE_MB * 1024L;
	public static long ONE_TB = ONE_GB * 1024L;
	public static long ONE_PB = ONE_TB * 1024L;

	private static Pattern FILE_SIZE_UNIT_PATTERN = Pattern.compile("(?i)([\\d\\.]*)([kmgtpb]{2})$");

	public static long parseHumanReadableFileSize(String fileSize) {
		if (StringUtils.isEmpty(fileSize)) {
			return 0;
		}
		Matcher m = FILE_SIZE_UNIT_PATTERN.matcher(fileSize.trim());
		if (m.find()) {
			String number = m.group(1);
			String unit = m.group(2);
			return (long) (Double.parseDouble(number) * parseUnit(unit));
		}
		return Long.parseLong(fileSize);
	}

	static Map<String, Long> unitMapping = new HashMap<String, Long>();
	static {
		unitMapping.put("kb", ONE_KB);
		unitMapping.put("mb", ONE_MB);
		unitMapping.put("gb", ONE_GB);
		unitMapping.put("tb", ONE_TB);
		unitMapping.put("pb", ONE_PB);
	}

	private static long parseUnit(String unit) {
		String lowerUnit = unit.toLowerCase();
		Long result = unitMapping.get(lowerUnit);
		if (result == null) {
			throw new IllegalArgumentException("unknow file size unit:" + unit);
		}
		return result;
	}

	public static String getHumanReadableFileSize(Long fileSize) {
		if (fileSize == null)
			return null;
		return getHumanReadableFileSize(fileSize.longValue());
	}

	public static String getHumanReadableFileSize(long fileSize) {
		if (fileSize < 0) {
			return String.valueOf(fileSize);
		}
		String result = getHumanReadableFileSize(fileSize, ONE_PB, "PB");
		if (result != null) {
			return result;
		}

		result = getHumanReadableFileSize(fileSize, ONE_TB, "TB");
		if (result != null) {
			return result;
		}
		result = getHumanReadableFileSize(fileSize, ONE_GB, "GB");
		if (result != null) {
			return result;
		}
		result = getHumanReadableFileSize(fileSize, ONE_MB, "MB");
		if (result != null) {
			return result;
		}
		result = getHumanReadableFileSize(fileSize, ONE_KB, "KB");
		if (result != null) {
			return result;
		}
		return String.valueOf(fileSize) + "B";
	}

	private static String getHumanReadableFileSize(long fileSize, long unit, String unitName) {
		if (fileSize == 0)
			return "0";

		if (fileSize / unit >= 1) {
			double value = fileSize / (double) unit;
			DecimalFormat df = new DecimalFormat("######.##" + unitName);
			return df.format(value);
		}
		return null;
	}
}
