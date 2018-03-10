package com.huya.lighthouse.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.AbstractBODefTask;
import com.huya.lighthouse.model.util.ModelConverters;
import com.huya.lighthouse.util.JsonUtils;
import com.huya.lighthouse.util.SSHAsynExecUtils;

/**
 * 
 *
 */
public abstract class AbstractPlugin {

	protected static Logger logger = LoggerFactory.getLogger(AbstractPlugin.class);

	private static Map<String, Object> objectMap = null;
	public final static Map<String, Object> contextMap = new HashMap<String, Object>();
	public final static String outputPathsKey = "outputPaths";
	public static String instanceDir = "";

	@SuppressWarnings("unchecked")
	public static synchronized AbstractBODefTask readObject() throws Exception {
		if (objectMap != null) {
			return (AbstractBODefTask) objectMap.get("boDefTask");
		}
		String objectPath = SSHAsynExecUtils.getObjectPath(instanceDir);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectPath));
		objectMap = (Map<String, Object>) ois.readObject();
		String taskBody = (String) objectMap.get("taskBody");
		int preLength = 0;
		int curLength = taskBody.length();
		while (preLength != curLength) {
			preLength = curLength;
			taskBody = taskBody.replaceAll("([^\\\\])\\\\r\\\\n", "$1\n");
			taskBody = taskBody.replaceAll("([^\\\\])\\\\n", "$1\n");
			taskBody = taskBody.replaceAll("([^\\\\])\\r\\n", "$1\n");
			taskBody = taskBody.replaceAll("([^\\\\])\\\\t", "$1\t");
			curLength = taskBody.length();
		}
		logger.info(taskBody);
		AbstractBODefTask boDefTask = ModelConverters.str2BO(taskBody);
		objectMap.put("boDefTask", boDefTask);
		return boDefTask;
	}

	public static Integer getTaskId() {
		if (objectMap == null) {
			return null;
		}
		return (Integer) objectMap.get("taskId");
	}

	public static Date getTaskDate() {
		if (objectMap == null) {
			return null;
		}
		return (Date) objectMap.get("taskDate");
	}

	public static Integer getCatalogId() {
		if (objectMap == null) {
			return null;
		}
		return (Integer) objectMap.get("catalogId");
	}

	@SuppressWarnings("unchecked")
	private static List<Map<String, Object>> getPreContexts() throws Exception {
		if (objectMap == null) {
			return null;
		}
		List<String> preContextStrs = (List<String>) objectMap.get("preContexts");
		if (preContextStrs == null) {
			return null;
		}
		List<Map<String, Object>> preContextMaps = new ArrayList<Map<String, Object>>();
		for (String preContext : preContextStrs) {
			Map<String, Object> preContextMap = JsonUtils.decode(preContext, Map.class);
			preContextMaps.add(preContextMap);
		}
		return preContextMaps;
	}

	public static List<String> getOutputPaths() throws Exception {
		List<Map<String, Object>> preContextMaps = getPreContexts();
		if (preContextMaps == null) {
			return null;
		}
		List<String> allOutputPaths = new ArrayList<String>();
		for (Map<String, Object> preContextMap : preContextMaps) {
			@SuppressWarnings("unchecked")
			List<String> outputPaths = (List<String>) preContextMap.get(outputPathsKey);
			if (outputPaths != null) {
				allOutputPaths.addAll(outputPaths);
			}
		}
		return allOutputPaths;
	}

	public static void writeContextFile() throws Exception {
		if (!contextMap.isEmpty()) {
			String context = JsonUtils.encode(contextMap);
			FileUtils.writeStringToFile(new File(SSHAsynExecUtils.getContextPath(instanceDir)), context);
		}
	}

}
