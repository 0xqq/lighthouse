package com.huya.lighthouse.server.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefLessee;
import com.huya.lighthouse.server.factory.DefLesseeFactory;
import com.huya.lighthouse.server.factory.ThreadPublicFactory;
import com.huya.lighthouse.util.HttpClientUtils2;
import com.huya.lighthouse.util.SleepUtils;

public class TriggerExtServiceUtils {

	protected static Logger logger = LoggerFactory.getLogger(TriggerExtServiceUtils.class);
	
	public static void trigger(Integer catalogId, Integer taskId, Date taskDate, String status, Integer maxRetryNum, Integer retriedNum) throws Exception {
		Thread.currentThread().setName("TriggerExtServiceUtils_" + taskId);
		
		DefLessee defLessee = DefLesseeFactory.getByCatalogId(catalogId);
		if (defLessee == null) {
			logger.error("defLessee is null for catalogId=" + catalogId);
			return;
		}
		String[] urlArr = StringUtils.split(defLessee.getTriggerUrls(), ",");
		if (urlArr == null || urlArr.length == 0) {
			return;
		}
		
		final Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("catalogId", catalogId.toString());
		paramMap.put("taskId", taskId.toString());
		paramMap.put("taskDate", DateFormatUtils.format(taskDate, "yyyyMMddHHmmss"));
		paramMap.put("status", status);
		paramMap.put("maxRetryNum", maxRetryNum.toString());
		paramMap.put("retriedNum", retriedNum.toString());
		
		for (final String url : urlArr) {
			ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					int i = 0;
					while (i < 60) {
						try {
							HttpClientUtils2.postParameters(url, paramMap);
							break;
						} catch (Throwable e) {
							logger.error(url, e);
							i++;
							SleepUtils.sleep(5000l);
						}
					}
				}
			});
		}
	}

}
