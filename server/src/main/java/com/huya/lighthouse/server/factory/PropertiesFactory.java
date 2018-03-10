package com.huya.lighthouse.server.factory;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.util.EnvUtils;

public class PropertiesFactory {

	public static String zkConn = null;
	public static String zkPath = null;
	public static String srcPluginDir = null;
	public static String hdfsURL = null;
	public static String[] admins = null;
	public static String[] adminsAlerts = null;

	public static synchronized void init() {
		String env = EnvUtils.getEnvType();
		Properties prop = new Properties();
		try {
			prop.load(PropertiesFactory.class.getClassLoader().getResourceAsStream("config-" + env + ".properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		zkConn = prop.getProperty("zkConn");
		zkPath = prop.getProperty("zkPath");
		srcPluginDir = prop.getProperty("srcPluginDir");
		hdfsURL = prop.getProperty("hdfsURL");
		admins = StringUtils.split(prop.getProperty("admins"), ",");
		adminsAlerts = StringUtils.split(prop.getProperty("adminsAlerts"), ",");
	}
}
