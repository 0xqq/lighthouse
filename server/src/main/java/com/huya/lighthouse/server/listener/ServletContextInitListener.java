package com.huya.lighthouse.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.huya.lighthouse.server.executor.LeaderSelector;
import com.huya.lighthouse.server.factory.PropertiesFactory;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.scheduler.QuartzScheduler;

public class ServletContextInitListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ServletContextInitListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("SceneRestorer");
		WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getAutowireCapableBeanFactory()
				.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		PropertiesFactory.init();
		ServiceBeanFactory.init();
		try {
			LeaderSelector.select();
		} catch (Exception e) {
			logger.error("LeaderSelector.select error", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("ServletContextListener shutdown all scheduler");
		try {
			QuartzScheduler.shutdownAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
