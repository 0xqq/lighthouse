package com.huya.lighthouse.util;

import java.util.Date;

import org.junit.Test;

public class DateUtils2Test {

	@Test
	public void test1() throws Exception {
		Date date = DateUtils2.getSunday(new Date());
		System.out.println(date);
	}
}
