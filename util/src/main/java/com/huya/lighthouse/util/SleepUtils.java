package com.huya.lighthouse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SleepUtils {

	private static Logger logger = LoggerFactory.getLogger(SleepUtils.class);

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			logger.error("sleep error", e);
		}
	}
}
