package com.huya.lighthouse.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateUtils2 {

	public static String[] parsePatterns = new String[] { "yyyyMMddHHmmss", "yyyyMMdd", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" };

	public static Date add(Date date, int year, int month, int day, int hour, int minute) {
		if (year != 0) {
			date = DateUtils.addYears(date, year);
		}
		if (month != 0) {
			date = DateUtils.addMonths(date, month);
		}
		if (day != 0) {
			date = DateUtils.addDays(date, day);
		}
		if (hour != 0) {
			date = DateUtils.addHours(date, hour);
		}
		if (minute != 0) {
			date = DateUtils.addMinutes(date, minute);
		}
		return date;
	}
	
	public static Date set(Date date, int hour, int minute, int seconds) {
		date = DateUtils.setHours(date, hour);
		date = DateUtils.setMinutes(date, minute);
		date = DateUtils.setSeconds(date, seconds);
		date = DateUtils.setMilliseconds(date, 0);
		return date;
	}

	public static Date clear2SecondLevel(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date clear2MinuteLevel(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date clear2HourLevel(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 2);
		return calendar.getTime();
	}

	public static Date getSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (Calendar.DAY_OF_WEEK == calendar.getFirstDayOfWeek()) {
			return date;
		} else {
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			return calendar.getTime();
		}
	}

	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date getFirstDayOfQuarter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int curMonth = calendar.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
		} else if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			calendar.set(Calendar.MONTH, Calendar.APRIL);
		} else if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			calendar.set(Calendar.MONTH, Calendar.JULY);
		} else if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			calendar.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date getLastDayOfQuarter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int curMonth = calendar.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			calendar.set(Calendar.MONTH, Calendar.MARCH);
		} else if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			calendar.set(Calendar.MONTH, Calendar.JUNE);
		} else if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			calendar.set(Calendar.MONTH, Calendar.AUGUST);
		} else if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		}
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date getDay2359(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		return calendar.getTime();
	}

	public static Date timestamp2Date(Date date) {
		if (date instanceof Timestamp) {
			date = new Date(date.getTime());
		}
		return date;
	}

	public static long getTime(Date date) {
		return date.getTime();
	}

	public static String dateStr(Date date) {
		if (date == null) {
			return "";
		}
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}
}
