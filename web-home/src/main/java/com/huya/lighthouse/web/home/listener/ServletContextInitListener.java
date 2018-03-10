package com.huya.lighthouse.web.home.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContextInitListener implements ServletContextListener {

	protected static Logger logger = LoggerFactory.getLogger(ServletContextInitListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
