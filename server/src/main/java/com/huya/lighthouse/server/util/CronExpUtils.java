package com.huya.lighthouse.server.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;

import com.huya.lighthouse.util.DateUtils2;

/**
 * cron表达式工具类
 * 
 */
public class CronExpUtils {

	public static List<Date> getTimeBeforeN(CronExpression cronExp, Date date, int num) {
		if (cronExp == null) {
			return null;
		}
		TreeSet<Date> beforeSet = new TreeSet<Date>();
		Date rollDate = DateUtils.addDays(date, -1);
		long rollEndTime = date.getTime();
		while (beforeSet.size() < num) {
			Date rollDateTmp = rollDate;
			while (true) {
				Date satisfyDate = cronExp.getTimeAfter(rollDate);
				if (satisfyDate.getTime() <= rollEndTime) {
					beforeSet.add(satisfyDate);
				} else {
					break;
				}
				rollDate = satisfyDate;
			}
			rollDate = DateUtils.addDays(rollDateTmp, -1);
			rollEndTime = rollDateTmp.getTime();
		}

		List<Date> resultList = new ArrayList<Date>();
		int i = 0;
		Date before = beforeSet.pollLast();
		while (before != null && i < num) {
			resultList.add(before);
			before = beforeSet.pollLast();
			i++;
		}
		return resultList;
	}

	public static List<Date> getTimeBetween(CronExpression cronExp, Date startDate, Date endDate, int num) {
		if (cronExp == null) {
			return null;
		}
		List<Date> resultList = new ArrayList<Date>();
		Date rollDate = startDate;
		long rollEndTime = endDate.getTime();
		int i = 0;
		while (true) {
			Date satisfyDate = cronExp.getTimeAfter(rollDate);
			if (satisfyDate.getTime() < rollEndTime) {
				resultList.add(satisfyDate);
				i++;
				if (i == num) {
					return resultList;
				}
			} else {
				break;
			}
			rollDate = satisfyDate;
		}
		return resultList;
	}
	
	public static List<Date> getTimeInHour(CronExpression cronExp, Date date) {
		Date yyyyMMddHH = DateUtils2.clear2MinuteLevel(date);
		Date yyyyMMddHHPreMilli = DateUtils.addMilliseconds(yyyyMMddHH, -1);
		Date yyyyMMddHHNext = DateUtils.addHours(yyyyMMddHH, 1);
		return getTimeBetween(cronExp, yyyyMMddHHPreMilli, yyyyMMddHHNext, 0);
	}

	public static List<Date> getTimeInDate(CronExpression cronExp, Date date) {
		Date yyyyMMdd = DateUtils2.clear2HourLevel(date);
		Date yyyyMMddPreMilli = DateUtils.addMilliseconds(yyyyMMdd, -1);
		Date yyyyMMddNext = DateUtils.addDays(yyyyMMdd, 1);
		return getTimeBetween(cronExp, yyyyMMddPreMilli, yyyyMMddNext, 0);
	}

	public static List<Date> getTimeInWeek(CronExpression cronExp, Date date, Date endDate) {
		Date yyyyMMdd = DateUtils2.clear2HourLevel(date);
		Date monday = DateUtils2.getMonday(yyyyMMdd);
		Date mondayPreMilli = DateUtils.addMilliseconds(monday, -1);
		Date mondayNext = DateUtils.addDays(monday, 7);
		if (endDate == null || endDate.getTime() > mondayNext.getTime()) {
			endDate = mondayNext;
		}
		return getTimeBetween(cronExp, mondayPreMilli, endDate, 0);
	}

	public static List<Date> getTimeInMonth(CronExpression cronExp, Date date, Date endDate) {
		Date yyyyMMdd = DateUtils2.clear2HourLevel(date);
		Date month1d = DateUtils2.getFirstDayOfMonth(yyyyMMdd);
		Date month1dPreMilli = DateUtils.addMilliseconds(month1d, -1);
		Date month1dNext = DateUtils.addMonths(month1d, 1);
		if (endDate == null || endDate.getTime() > month1dNext.getTime()) {
			endDate = month1dNext;
		}
		return getTimeBetween(cronExp, month1dPreMilli, endDate, 0);
	}

	public static List<Date> getTimeInQuarter(CronExpression cronExp, Date date, Date endDate) {
		Date yyyyMMdd = DateUtils2.clear2HourLevel(date);
		Date quarter1d = DateUtils2.getFirstDayOfQuarter(yyyyMMdd);
		Date quarter1dPreMilli = DateUtils.addMilliseconds(quarter1d, -1);
		Date quarterLastd = DateUtils2.getLastDayOfQuarter(yyyyMMdd);
		Date quarter1dNext = DateUtils.addDays(quarterLastd, 1);
		if (endDate == null || endDate.getTime() > quarter1dNext.getTime()) {
			endDate = quarter1dNext;
		}
		return getTimeBetween(cronExp, quarter1dPreMilli, endDate, 0);
	}
}
