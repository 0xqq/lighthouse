package com.huya.lighthouse.service.util;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.util.EnvUtils;
import com.huya.lighthouse.util.HttpClientUtils2;

public class LeaderFinder {

	private static Logger logger = LoggerFactory.getLogger(LeaderFinder.class);

	public static String[] serverURLs = null;
	public static String leaderURL = null;
	private static boolean isInit = false;

	public static synchronized void init() {
		if (isInit) {
			return;
		}
		isInit = true;
		load();
		findLeader();
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					findLeader();
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}, 60, 60, TimeUnit.SECONDS);
	}

	public static void findLeader() {
		for (String serverURL : serverURLs) {
			String requestURL = serverURL + "/isLeader.do";
			try {
				String isLeaderStr = HttpClientUtils2.get(requestURL);
				if (StringUtils.equals(isLeaderStr, "true")) {
					leaderURL = serverURL;
					logger.info("leaderURL=" + leaderURL);
					break;
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	public static synchronized void load() {
		String env = EnvUtils.getEnvType();
		Properties prop = new Properties();
		try {
			prop.load(LeaderFinder.class.getClassLoader().getResourceAsStream("leader-" + env + ".properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		serverURLs = StringUtils.split(prop.getProperty("serverURLs"), ",");
	}
}
