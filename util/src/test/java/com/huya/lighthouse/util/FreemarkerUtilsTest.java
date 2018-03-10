package com.huya.lighthouse.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class FreemarkerUtilsTest {

	@Test
	public void test() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Date tdate = DateUtils.parseDate("2018-01-15 09:53:00", DateUtils2.parsePatterns);
		paramMap.put("tdate", tdate);
		String sql = "";
		String result = "";
		
//		sql = "${DateUtils2.getFirstDayOfMonth(tdate)?string('yyyy-MM-dd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${DateUtils2.getLastDayOfMonth(tdate)?string('yyyy-MM-dd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${DateUtils2.getFirstDayOfMonth(DateUtils.addMonths(tdate, -1))?string('yyyy-MM-dd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${DateUtils.addMonths(tdate, -1)?string('yyyyMM')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${DateUtils.addDays(DateUtils2.getFirstDayOfMonth(tdate), 2)?string('yyyy-MM-dd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${DateUtils.addDays(tdate, -30)?string('yyyy-MM-dd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${tdate?string('yyyyMMdd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${tdate?string('H')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
//		sql = "${DateUtils2.getMonday(tdate)?string('yyyy-MM-dd')}";
//		result = FreemarkerUtils.parse2Time(sql, paramMap);
//		System.out.println(result);
		
		sql = "${DateUtils2.getTime(DateUtils2.set(tdate, 23, 59, 59)) / 1000}";
		result = FreemarkerUtils.parse2Time(sql, paramMap);
		System.out.println(result);
	}
}
