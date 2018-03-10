package com.huya.lighthouse.service.base;

import org.junit.After;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 
 */
public class BaseServiceTestCase {

	private static String[] configLocations = { "classpath*:spring/applicationContext-*.xml" };
	private static AbstractApplicationContext applicationContext;

	public synchronized static Object getBean(String beanName) {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext(configLocations);
		}
		return applicationContext.getBean(beanName);
	}

	public static void shutDown() {
		if (applicationContext != null) {
			applicationContext.registerShutdownHook();
		}
	}

	@After
	public void tearDown() {
		shutDown();
	}

	public static void main(String[] args) {
		System.out.println(getBean("sqlPrivilegeInputChecker"));
	}

}
