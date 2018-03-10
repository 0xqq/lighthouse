package com.huya.lighthouse.server.util;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefLessee;
import com.huya.lighthouse.server.factory.DefLesseeFactory;
import com.huya.lighthouse.server.factory.ThreadPublicFactory;
import com.huya.lighthouse.util.HttpClientUtils2;
import com.huya.lighthouse.util.JsonUtils;
import com.huya.lighthouse.util.SleepUtils;

public class AlertUtils {
	
	private static Logger logger = LoggerFactory.getLogger(AlertUtils.class);
	
	public static void alertMonitorBegin(final Integer catalogId, final Integer taskId, final String taskName, final Date taskDate, final String cronExp, final Integer moveMonth, final Integer moveDay, final Integer moveHour, final Integer moveMinute) {
		ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("alertMonitorBegin_" + taskId);
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("type", "monitorBegin");
					paramMap.put("taskId", taskId);
					paramMap.put("taskName", taskName);
					paramMap.put("taskDate", DateFormatUtils.format(taskDate, "yyyyMMddHHmmss"));
					paramMap.put("cronExp", cronExp);
					paramMap.put("moveMonth", moveMonth);
					paramMap.put("moveDay", moveDay);
					paramMap.put("moveHour", moveHour);
					paramMap.put("moveMinute", moveMinute);
					alert(catalogId, paramMap);
				} catch (Exception e) {
					logger.error(taskId.toString(), e);
				}
			}
		});
	}
	
	public static void alertMonitorRetry(final Integer catalogId, final Integer taskId, final String taskName, final Date taskDate, final Integer retriedN) {
		ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("alertMonitorRetry_" + taskId);
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("type", "monitorRetry");
					paramMap.put("taskId", taskId);
					paramMap.put("taskName", taskName);
					paramMap.put("taskDate", DateFormatUtils.format(taskDate, "yyyyMMddHHmmss"));
					paramMap.put("retriedN", retriedN);
					alert(catalogId, paramMap);
				} catch (Exception e) {
					logger.error(taskId.toString(), e);
				}
			}
		});
	}
	
	public static void alertMonitorDur(final Integer catalogId, final Integer taskId, final String taskName, final Date taskDate, final Integer expectDur, final Long factDur) {
		ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("alertMonitorDur_" + taskId);
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("type", "monitorDur");
					paramMap.put("taskId", taskId);
					paramMap.put("taskName", taskName);
					paramMap.put("taskDate", DateFormatUtils.format(taskDate, "yyyyMMddHHmmss"));
					paramMap.put("expectDur", expectDur);
					paramMap.put("factDur", factDur);
					alert(catalogId, paramMap);
				} catch (Exception e) {
					logger.error(taskId.toString(), e);
				}
			}
		});
	}
	
	public static void alertOffline(final Integer catalogId, final Integer taskId, final String taskName, final Date offlineTime) {
		ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("alertOffline_" + taskId);
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("type", "monitorOffline");
					paramMap.put("taskId", taskId);
					paramMap.put("taskName", taskName);
					paramMap.put("offlineTime", DateFormatUtils.format(offlineTime, "yyyyMMddHHmmss"));
					alert(catalogId, paramMap);
				} catch (Exception e) {
					logger.error(taskId.toString(), e);
				}
			}
		});
	}
	
	private static void alert(Integer catalogId, Map<String, Object> paramMap) throws Exception {
		DefLessee defLessee = DefLesseeFactory.getByCatalogId(catalogId);
		if (defLessee == null || StringUtils.isBlank(defLessee.getAlertUrl())) {
			logger.error("alertUrl is null for catalogId=" + catalogId + ", paramMap=" + paramMap);
			return;
		}
		String json = JsonUtils.encode(paramMap);
		String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		String key = DigestUtils.md5Hex(defLessee.getPassword() + date + json);
		
		StringBuilder targetParamsBuilder = new StringBuilder();
		targetParamsBuilder.append("key=").append(key);
		targetParamsBuilder.append("&date=").append(date);
		targetParamsBuilder.append("&json=").append(URLEncoder.encode(json, "UTF-8"));
		String targetParams = targetParamsBuilder.toString();
		String targetUrl = defLessee.getAlertUrl() + "?" + targetParams;
		logger.info("alert info: " + targetUrl);
		
		int i = 0;
		while (i < 60) {
			try {
				HttpClientUtils2.postParameters(defLessee.getAlertUrl(), targetParams);
				break;
			} catch (Exception e) {
				logger.error("retry " + i + ", " + targetUrl, e);
				i++;
				SleepUtils.sleep(5000l);
			}
		}
	}
}
