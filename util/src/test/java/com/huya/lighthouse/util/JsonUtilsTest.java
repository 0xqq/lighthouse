package com.huya.lighthouse.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonUtilsTest {

	@Test
	public void test() throws Exception {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		List<String> outputPaths = new ArrayList<String>();
		outputPaths.add("hdfs://gamelive/tmp/lighthouse/20170329/bbb/0");
		outputPaths.add("hdfs://gamelive/tmp/lighthouse/20170329/bbb/1");
		outputPaths.add("hdfs://gamelive/tmp/lighthouse/20170329/bbb/2");
		contextMap.put("outputPaths", outputPaths);
		String json = JsonUtils.encode(contextMap);
		System.out.println(json);
		@SuppressWarnings("unchecked")
		Map<String, Object> contextMap1 = JsonUtils.decode(json, Map.class);
		System.out.println(contextMap1);
		@SuppressWarnings("unchecked")
		List<String> outputPaths1 = (List<String>) contextMap1.get("outputPaths");
		for (String outputPath : outputPaths1) {
			System.out.println(outputPath);
		}
	}
}
