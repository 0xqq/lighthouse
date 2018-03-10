package com.huya.lighthouse.server.factory;

import com.huya.lighthouse.dao.util.ApplicationContextHolder;
import com.huya.lighthouse.service.BODefTaskService;
import com.huya.lighthouse.service.DefAgentGroupService;
import com.huya.lighthouse.service.DefCatalogService;
import com.huya.lighthouse.service.DefLesseeService;
import com.huya.lighthouse.service.DefMonitorBeginService;
import com.huya.lighthouse.service.DefMonitorDurService;
import com.huya.lighthouse.service.DefMonitorRetryService;
import com.huya.lighthouse.service.DefQueueService;
import com.huya.lighthouse.service.DefTaskDependService;
import com.huya.lighthouse.service.DefTaskParamService;
import com.huya.lighthouse.service.DefTaskService;
import com.huya.lighthouse.service.InstanceTaskDependService;
import com.huya.lighthouse.service.InstanceTaskLogService;
import com.huya.lighthouse.service.InstanceTaskService;

/**
 * Bean服务工厂
 * 
 */
public class ServiceBeanFactory {

	private static DefTaskService defTaskService;
	private static DefTaskDependService defTaskDependService;
	private static DefTaskParamService defTaskParamService;
	private static InstanceTaskService instanceTaskService;
	private static InstanceTaskDependService instanceTaskDependService;
	private static DefQueueService defQueueService;
	private static DefAgentGroupService defAgentGroupService;
	private static DefCatalogService defCatalogService;
	private static DefMonitorBeginService defMonitorBeginService;
	private static DefMonitorDurService defMonitorDurService;
	private static DefMonitorRetryService defMonitorRetryService;
	private static BODefTaskService bODefTaskService;
	private static DefLesseeService defLesseeService;
	private static InstanceTaskLogService instanceTaskLogService;

	public static void init() {
		defTaskService = (DefTaskService) ApplicationContextHolder.getBean("defTaskService");
		defTaskDependService = (DefTaskDependService) ApplicationContextHolder.getBean("defTaskDependService");
		defTaskParamService = (DefTaskParamService) ApplicationContextHolder.getBean("defTaskParamService");
		instanceTaskService = (InstanceTaskService) ApplicationContextHolder.getBean("instanceTaskService");
		instanceTaskDependService = (InstanceTaskDependService) ApplicationContextHolder.getBean("instanceTaskDependService");
		defQueueService = (DefQueueService) ApplicationContextHolder.getBean("defQueueService");
		defAgentGroupService = (DefAgentGroupService) ApplicationContextHolder.getBean("defAgentGroupService");
		defCatalogService = (DefCatalogService) ApplicationContextHolder.getBean("defCatalogService");
		defMonitorBeginService = (DefMonitorBeginService) ApplicationContextHolder.getBean("defMonitorBeginService");
		defMonitorDurService = (DefMonitorDurService) ApplicationContextHolder.getBean("defMonitorDurService");
		defMonitorRetryService = (DefMonitorRetryService) ApplicationContextHolder.getBean("defMonitorRetryService");
		bODefTaskService = (BODefTaskService) ApplicationContextHolder.getBean("bODefTaskService");
		defLesseeService = (DefLesseeService) ApplicationContextHolder.getBean("defLesseeService");
		instanceTaskLogService = (InstanceTaskLogService) ApplicationContextHolder.getBean("instanceTaskLogService");
	}

	public static DefTaskService getDefTaskService() {
		return defTaskService;
	}

	public static DefTaskDependService getDefTaskDependService() {
		return defTaskDependService;
	}

	public static DefTaskParamService getDefTaskParamService() {
		return defTaskParamService;
	}

	public static InstanceTaskService getInstanceTaskService() {
		return instanceTaskService;
	}

	public static InstanceTaskDependService getInstanceTaskDependService() {
		return instanceTaskDependService;
	}

	public static DefQueueService getDefQueueService() {
		return defQueueService;
	}

	public static DefAgentGroupService getDefAgentGroupService() {
		return defAgentGroupService;
	}

	public static DefCatalogService getDefCatalogService() {
		return defCatalogService;
	}

	public static DefMonitorBeginService getDefMonitorBeginService() {
		return defMonitorBeginService;
	}

	public static DefMonitorDurService getDefMonitorDurService() {
		return defMonitorDurService;
	}

	public static DefMonitorRetryService getDefMonitorRetryService() {
		return defMonitorRetryService;
	}

	public static BODefTaskService getbODefTaskService() {
		return bODefTaskService;
	}

	public static DefLesseeService getDefLesseeService() {
		return defLesseeService;
	}

	public static InstanceTaskLogService getInstanceTaskLogService() {
		return instanceTaskLogService;
	}
}
