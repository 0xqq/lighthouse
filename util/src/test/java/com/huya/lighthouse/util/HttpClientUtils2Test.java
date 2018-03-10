package com.huya.lighthouse.util;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.junit.Test;

public class HttpClientUtils2Test {

	@Test
	public void test1() {
		try {
			List<String> cronExpList = new ArrayList<String>();
			cronExpList.add("0 5 0/* * * ?");
			cronExpList.add("0 10 0/* * * ?");
			String cronExpListStr = JsonUtils.encode(cronExpList);
			System.out.println(cronExpListStr);
//			Map<String, String> paramMap = new HashMap<String, String>();
//			paramMap.put("cronExpList", cronExpListStr);
//			paramMap.put("date", "20170308170000");
//			paramMap.put("taskId", "10");
//			String str = HttpClientUtils2.postParameters("http://127.0.0.1:8080/addCronMonitorBegin.do", paramMap);
			String jsonBody = "{\"key1\":\"val1\"}";
			System.out.println(jsonBody);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("list[0]", "v1");
			paramMap.put("list[1]", "v2");
			String str = HttpClientUtils2.postParameters("http://127.0.0.1:8080/test.do", paramMap);
			
			
			
			// String str=
			// get("https://localhost:443/ssl/test.shtml?name=12&page=34","GBK");
			/*
			 * Map<String,String> map = new HashMap<String,String>();
			 * map.put("name", "111"); map.put("page", "222"); String str=
			 * postForm("https://localhost:443/ssl/test.shtml",map,null, 10000,
			 * 10000);
			 */
			System.out.println(str);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() throws Exception {
		String url = "https://portal-data-dev-monitor-agent.yy.com/taskfinished";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("catalogId", "10002");
		paramMap.put("taskId", "13135");
		paramMap.put("taskDate", "20171204000000");
		paramMap.put("status", "FAIL");
		paramMap.put("maxRetryNum", "3");
		paramMap.put("retriedNum", "1");
		String str = HttpClientUtils2.postParameters(url, paramMap);
		System.out.println(str);
	}
}
