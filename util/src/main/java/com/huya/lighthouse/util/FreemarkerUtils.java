package com.huya.lighthouse.util;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Freemarker工具类
 * 
 * @author chenwu
 * 
 */
public class FreemarkerUtils {

	/**
	 * 用Freemarker解析字符串脚本(默认参数和配置)
	 * 
	 * @param script
	 * @return
	 */
	public static String parse(String script) {
		return parse(script, getDefaultParamMap(), getDefaultConf());
	}

	/**
	 * 
	 * @param script
	 * @param paramMap
	 * @return
	 */
	public static String parse(String script, Map<String, Object> paramMap) {
		paramMap.putAll(getDefaultParamMap());
		return parse(script, paramMap, getDefaultConf());
	}

	/**
	 * 用两次Freemarker解析字符串脚本
	 * 
	 * @param script
	 * @param paramMap
	 * @return
	 */
	public static String parse2Time(String script, Map<String, Object> paramMap) {
		paramMap.putAll(getDefaultParamMap());
		String resultLevel1 = parse(script, paramMap, getDefaultConf());
		return parse(resultLevel1, paramMap, getDefaultConf());
	}

	/**
	 * 用Freemarker解析字符串脚本
	 * 
	 * @param script
	 * @param paramMap
	 * @return
	 */
	public static String parse(String script, Map<String, Object> paramMap, Configuration conf) {
		Reader reader = null;
		Writer writer = null;
		String result;
		try {
			reader = new StringReader(script);
			Template template = new Template("" + reader, reader, conf);
			writer = new StringWriter();
			template.process(paramMap, writer);
			result = writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(reader);
		}
		return result;
	}

	/**
	 * 默认配置
	 * 
	 * @return
	 */
	public static Configuration getDefaultConf() {
		Configuration conf = new Configuration();
		conf.setNumberFormat("###############");
		conf.setBooleanFormat("true,false");
		return conf;
	}

	/**
	 * 默认参数
	 * 
	 * @return
	 */
	public static Map<String, Object> getDefaultParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("DateUtils", new DateUtils());
		paramMap.put("DateUtils2", new DateUtils2());
		paramMap.put("sysTime", new Date());
		return paramMap;
	}
}