package com.huya.lighthouse.util;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络连接工具类
 * 
 */
public class URLUtils2 {

	protected static final Logger LOG = LoggerFactory.getLogger(URLUtils2.class);

	public static String POST = "POST";
	public static String GET = "GET";
	public static String FormEncodedUTF8 = "application/x-www-form-urlencoded;charset=UTF-8";
	public static String JsonEncodedUTF8 = "application/json;charset=UTF-8";

	public static String post(String url, String params, int maxRetry, long sleepMillis) throws Exception {
		for (int i = 0; i < maxRetry; i++) {
			try {
				HttpURLConnection c = postConnect(url);
				c.getOutputStream().write(params.getBytes());
				c.getOutputStream().flush();
				String result = IOUtils.toString(c.getInputStream());
				int resCode = c.getResponseCode();
				if (resCode != 200) {
					throw new Exception("response code = " + resCode + ", " + result);
				}
				return result;
			} catch (Exception e) {
				if (i < maxRetry - 1) {
					LOG.error("retry " + i, e);
					SleepUtils.sleep(sleepMillis);
				} else {
					throw e;
				}
			}
		}
		return "500";
	}

	public static String post(String url, String params, String contentType, int maxRetry, long sleepMillis) throws Exception {
		for (int i = 0; i < maxRetry; i++) {
			try {
				HttpURLConnection c = urlConnect(url, POST, contentType);
				c.getOutputStream().write(params.getBytes());
				c.getOutputStream().flush();
				String result = IOUtils.toString(c.getInputStream());
				int resCode = c.getResponseCode();
				if (resCode != 200) {
					throw new Exception("response code = " + resCode + ", " + result);
				}
				return result;
			} catch (Exception e) {
				if (i < maxRetry - 1) {
					LOG.error("retry " + i, e);
					SleepUtils.sleep(sleepMillis);
				} else {
					throw e;
				}
			}
		}
		return "500";
	}

	public static String get(String url, int maxRetry, long sleepMillis) throws Exception {
		for (int i = 0; i < maxRetry; i++) {
			try {
				HttpURLConnection c = getConnect(url);
				String result = IOUtils.toString(c.getInputStream());
				int resCode = c.getResponseCode();
				if (resCode != 200) {
					throw new Exception("response code = " + resCode + ", " + result);
				}
				return result;
			} catch (Exception e) {
				if (i < maxRetry - 1) {
					LOG.error("retry " + i, e);
					SleepUtils.sleep(sleepMillis);
				} else {
					throw e;
				}
			}
		}
		return "500";
	}

	public static HttpURLConnection postConnect(String url) throws Exception {
		return urlConnect(url, POST, FormEncodedUTF8);
	}

	public static HttpURLConnection getConnect(String url) throws Exception {
		return urlConnect(url, GET, FormEncodedUTF8);
	}

	public static HttpURLConnection urlConnect(String url, String requestMethod, String contentType) throws Exception {
		HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setConnectTimeout(1000 * 60);
		httpUrlConnection.setReadTimeout(1000 * 60);
		httpUrlConnection.setRequestMethod(requestMethod);
		httpUrlConnection.setRequestProperty("Content-type", contentType);
		httpUrlConnection.connect();
		return httpUrlConnection;
	}

}
