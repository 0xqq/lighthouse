package com.huya.lighthouse.util;

public class AssertUtils {

	public static void assertTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}
	
}
