package com.huya.lighthouse.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huya.lighthouse.model.bo.def.AbstractBODefTask;
import com.huya.lighthouse.model.bo.def.BODefTaskAllInfo;
import com.huya.lighthouse.model.bo.def.DefTaskCron;
import com.huya.lighthouse.model.po.def.DefClassify;
import com.huya.lighthouse.model.po.def.DefMonitorBegin;
import com.huya.lighthouse.model.po.def.DefMonitorDur;
import com.huya.lighthouse.model.po.def.DefMonitorRetry;
import com.huya.lighthouse.model.po.def.DefObjectDepend;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.def.DefTaskDepend;
import com.huya.lighthouse.model.type.DependType;
import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.model.util.ModelConverters;
import com.huya.lighthouse.service.BODefTaskService;
import com.huya.lighthouse.service.DefClassifyService;
import com.huya.lighthouse.service.DefMonitorBeginService;
import com.huya.lighthouse.service.DefMonitorDurService;
import com.huya.lighthouse.service.DefMonitorRetryService;
import com.huya.lighthouse.service.DefObjectDependService;
import com.huya.lighthouse.service.DefTaskDependService;
import com.huya.lighthouse.service.DefTaskService;
import com.huya.lighthouse.service.util.LeaderFinder;
import com.huya.lighthouse.util.AssertUtils;
import com.huya.lighthouse.util.HttpClientUtils2;

/**
 * 
 */
@Service("bODefTaskService")
@Transactional(rollbackFor = Throwable.class)
public class BODefTaskServiceImpl implements BODefTaskService {

	protected static final Logger log = LoggerFactory.getLogger(BODefTaskServiceImpl.class);

	@Autowired
	private DefTaskService defTaskService;
	@Autowired
	private DefTaskDependService defTaskDependService;
	@Autowired
	private DefObjectDependService defObjectDependService;
	@Autowired
	private DefMonitorBeginService defMonitorBeginService;
	@Autowired
	private DefMonitorDurService defMonitorDurService;
	@Autowired
	private DefMonitorRetryService defMonitorRetryService;
	@Autowired
	private DefClassifyService defClassifyService;
	private boolean isProd = StringUtils.equals(System.getenv("DWENV"), "prod");
	
	@Override
	public void create(BODefTaskAllInfo boDefTaskAllInfo) throws Exception {
		AbstractBODefTask boDefTask = boDefTaskAllInfo.getBoDefTask();
		List<DefTaskDepend> defTaskDependList = boDefTaskAllInfo.getDefTaskDependList();
		List<DefObjectDepend> defObjectDependList = boDefTaskAllInfo.getDefObjectDependList();
		List<DefMonitorBegin> defMonitorBeginList = boDefTaskAllInfo.getDefMonitorBeginList();
		List<DefMonitorDur> defMonitorDurList = boDefTaskAllInfo.getDefMonitorDurList();
		List<DefMonitorRetry> defMonitorRetryList = boDefTaskAllInfo.getDefMonitorRetryList();
		List<DefClassify> defClassifyList = boDefTaskAllInfo.getDefClassifyList();

		Date date = new Date();
		boDefTask.setCreateTime(date);
		boDefTask.setUpdateTime(date);
		String user = boDefTask.getCreateUser();
		boDefTask.setUpdateUser(user);
		boDefTask.setIsValid(1);

		boDefTask.doAssert();
		boDefTask.doTrim();

		DefTask defTask = ModelConverters.defBO2PO(boDefTask);
		defTaskService.create(defTask);
		Integer taskId = defTask.getTaskId();
		boDefTask.setTaskId(taskId);

		if (defTaskDependList != null && defTaskDependList.size() > 0) {
			for (DefTaskDepend defTaskDepend : defTaskDependList) {
				if (StringUtils.equals(defTaskDepend.getDependType(), DependType.SELF.name())) {
					defTaskDepend.setPreTaskId(taskId);
				}
				defTaskDepend.setTaskId(taskId);
				defTaskDepend.setIsValid(1);
				defTaskDepend.setCreateTime(date);
				defTaskDepend.setUpdateTime(date);
				defTaskDepend.setCreateUser(user);
				defTaskDepend.setUpdateUser(user);
				defTaskDepend.doAssert();
			}
			defTaskDependService.batchCreate(defTaskDependList);
		}

		if (defObjectDependList != null && defObjectDependList.size() > 0) {
			for (DefObjectDepend defObjectDepend : defObjectDependList) {
				defObjectDepend.setTaskId(taskId);
				defObjectDepend.setIsValid(1);
				defObjectDepend.setCreateTime(date);
				defObjectDepend.setUpdateTime(date);
				defObjectDepend.setCreateUser(user);
				defObjectDepend.setUpdateUser(user);
				defObjectDepend.doAssert();
				defObjectDepend.doTrim();
			}
			defObjectDependService.batchCreate(defObjectDependList);
		}

		String cronExps = "";
		if (defMonitorBeginList != null && defMonitorBeginList.size() > 0) {
			for (DefMonitorBegin defMonitorBegin : defMonitorBeginList) {
				defMonitorBegin.setTaskId(taskId);
				defMonitorBegin.setIsValid(1);
				defMonitorBegin.setCreateTime(date);
				defMonitorBegin.setUpdateTime(date);
				defMonitorBegin.setCreateUser(user);
				defMonitorBegin.setUpdateUser(user);
				defMonitorBegin.doAssert();
				defMonitorBegin.doTrim();
				cronExps += ";" + defMonitorBegin.getCronExp();
			}
			defMonitorBeginService.batchCreate(defMonitorBeginList);
		}

		if (defMonitorDurList != null && defMonitorDurList.size() > 0) {
			for (DefMonitorDur defMonitorDur : defMonitorDurList) {
				defMonitorDur.setTaskId(taskId);
				defMonitorDur.setIsValid(1);
				defMonitorDur.setCreateTime(date);
				defMonitorDur.setUpdateTime(date);
				defMonitorDur.setCreateUser(user);
				defMonitorDur.setUpdateUser(user);
				defMonitorDur.doAssert();
				defMonitorDur.doTrim();
			}
			defMonitorDurService.batchCreate(defMonitorDurList);
		}

		if (defMonitorRetryList != null && defMonitorRetryList.size() > 0) {
			for (DefMonitorRetry defMonitorRetry : defMonitorRetryList) {
				defMonitorRetry.setTaskId(taskId);
				defMonitorRetry.setIsValid(1);
				defMonitorRetry.setCreateTime(date);
				defMonitorRetry.setUpdateTime(date);
				defMonitorRetry.setCreateUser(user);
				defMonitorRetry.setUpdateUser(user);
				defMonitorRetry.doAssert();
				defMonitorRetry.doTrim();
			}
			defMonitorRetryService.batchCreate(defMonitorRetryList);
		}
		
		if (defClassifyList != null && defClassifyList.size() > 0) {
			for (DefClassify defClassify : defClassifyList) {
				defClassify.setTaskId(taskId);
				defClassify.setIsValid(1);
				defClassify.setCreateTime(date);
				defClassify.setUpdateTime(date);
				defClassify.setCreateUser(user);
				defClassify.setUpdateUser(user);
				defClassify.doAssert();
				defClassify.doTrim();
			}
			defClassifyService.batchCreate(defClassifyList);
		}

		if (isProd) {
			if (boDefTask instanceof DefTaskCron) {
				String cronExp = ((DefTaskCron) boDefTask).getCronExp();
				String url = LeaderFinder.leaderURL + "/addCronTask.do";
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("taskId", taskId.toString());
				paramMap.put("cronExp", cronExp);
				HttpClientUtils2.postParameters(url, paramMap);
			}
	
			if (StringUtils.isNotBlank(cronExps)) {
				String url = LeaderFinder.leaderURL + "/addCronMonitorBegin.do";
				String params = "cronExps=" + cronExps;
				HttpClientUtils2.postParameters(url, params);
			}
		}
	}

	@Override
	public void update(BODefTaskAllInfo boDefTaskAllInfo) throws Exception {
		AbstractBODefTask boDefTask = boDefTaskAllInfo.getBoDefTask();
		List<DefTaskDepend> defTaskDependList = boDefTaskAllInfo.getDefTaskDependList();
		List<DefObjectDepend> defObjectDependList = boDefTaskAllInfo.getDefObjectDependList();
		List<DefMonitorBegin> defMonitorBeginList = boDefTaskAllInfo.getDefMonitorBeginList();
		List<DefMonitorDur> defMonitorDurList = boDefTaskAllInfo.getDefMonitorDurList();
		List<DefMonitorRetry> defMonitorRetryList = boDefTaskAllInfo.getDefMonitorRetryList();
		List<DefClassify> defClassifyList = boDefTaskAllInfo.getDefClassifyList();

		if (StringUtils.equals(boDefTask.getTaskType(), TaskType.CRON.name())) {
			throw new Exception("Cron task cannot been edit. Pleaser create new one.");
		}

		Date date = new Date();
		boDefTask.setUpdateTime(date);
		String user = boDefTask.getUpdateUser();
		Integer isValid = boDefTask.getIsValid();

		AssertUtils.assertTrue(boDefTask.getTaskId() != null, "taskId cannot be null!");
		boDefTask.doAssert();
		boDefTask.doTrim();

		DefTask defTask = ModelConverters.defBO2PO(boDefTask);
		defTaskService.update(defTask);
		Integer taskId = defTask.getTaskId();

		defTaskDependService.removeByTaskId(taskId);
		if (defTaskDependList != null && defTaskDependList.size() > 0) {
			checkLoop(taskId, defTaskDependList);
			for (DefTaskDepend defTaskDepend : defTaskDependList) {
				if (StringUtils.equals(defTaskDepend.getDependType(), DependType.SELF.name())) {
					defTaskDepend.setPreTaskId(taskId);
				}
				defTaskDepend.setTaskId(taskId);
				defTaskDepend.setIsValid(isValid);
				defTaskDepend.setCreateTime(date);
				defTaskDepend.setUpdateTime(date);
				defTaskDepend.setCreateUser(user);
				defTaskDepend.setUpdateUser(user);
				defTaskDepend.doAssert();
			}
			defTaskDependService.batchCreate(defTaskDependList);
		}

		defObjectDependService.removeByTaskId(taskId);
		if (defObjectDependList != null && defObjectDependList.size() > 0) {
			for (DefObjectDepend defObjectDepend : defObjectDependList) {
				defObjectDepend.setTaskId(taskId);
				defObjectDepend.setIsValid(isValid);
				defObjectDepend.setCreateTime(date);
				defObjectDepend.setUpdateTime(date);
				defObjectDepend.setCreateUser(user);
				defObjectDepend.setUpdateUser(user);
				defObjectDepend.doAssert();
				defObjectDepend.doTrim();
			}
			defObjectDependService.batchCreate(defObjectDependList);
		}

		String cronExps = "";
		defMonitorBeginService.removeByTaskId(taskId);
		if (defMonitorBeginList != null && defMonitorBeginList.size() > 0) {
			for (DefMonitorBegin defMonitorBegin : defMonitorBeginList) {
				defMonitorBegin.setTaskId(taskId);
				defMonitorBegin.setIsValid(isValid);
				defMonitorBegin.setCreateTime(date);
				defMonitorBegin.setUpdateTime(date);
				defMonitorBegin.setCreateUser(user);
				defMonitorBegin.setUpdateUser(user);
				defMonitorBegin.doAssert();
				defMonitorBegin.doTrim();
				cronExps += ";" + defMonitorBegin.getCronExp();
			}
			defMonitorBeginService.batchCreate(defMonitorBeginList);
		}

		defMonitorDurService.removeByTaskId(taskId);
		if (defMonitorDurList != null && defMonitorDurList.size() > 0) {
			for (DefMonitorDur defMonitorDur : defMonitorDurList) {
				defMonitorDur.setTaskId(taskId);
				defMonitorDur.setIsValid(isValid);
				defMonitorDur.setCreateTime(date);
				defMonitorDur.setUpdateTime(date);
				defMonitorDur.setCreateUser(user);
				defMonitorDur.setUpdateUser(user);
				defMonitorDur.doAssert();
				defMonitorDur.doTrim();
			}
			defMonitorDurService.batchCreate(defMonitorDurList);
		}

		defMonitorRetryService.removeByTaskId(taskId);
		if (defMonitorRetryList != null && defMonitorRetryList.size() > 0) {
			for (DefMonitorRetry defMonitorRetry : defMonitorRetryList) {
				defMonitorRetry.setTaskId(taskId);
				defMonitorRetry.setIsValid(isValid);
				defMonitorRetry.setCreateTime(date);
				defMonitorRetry.setUpdateTime(date);
				defMonitorRetry.setCreateUser(user);
				defMonitorRetry.setUpdateUser(user);
				defMonitorRetry.doAssert();
				defMonitorRetry.doTrim();
			}
			defMonitorRetryService.batchCreate(defMonitorRetryList);
		}
		
		defClassifyService.removeByTaskId(taskId);
		if (defClassifyList != null && defClassifyList.size() > 0) {
			for (DefClassify defClassify : defClassifyList) {
				defClassify.setTaskId(taskId);
				defClassify.setIsValid(1);
				defClassify.setCreateTime(date);
				defClassify.setUpdateTime(date);
				defClassify.setCreateUser(user);
				defClassify.setUpdateUser(user);
				defClassify.doAssert();
				defClassify.doTrim();
			}
			defClassifyService.batchCreate(defClassifyList);
		}

		if (isProd) {
			if (StringUtils.isNotBlank(cronExps)) {
				String url = LeaderFinder.leaderURL + "/addCronMonitorBegin.do";
				String params = "cronExps=" + cronExps;
				HttpClientUtils2.postParameters(url, params);
			}
		}
	}
	
	@Override
	public void rebuildInit(int taskId) throws Exception {
		if (isProd) {
			String url = LeaderFinder.leaderURL + "/rebuildInit.do?taskId=" + taskId;
			HttpClientUtils2.get(url);
		}
	}

	private void checkLoop(Integer taskId, List<DefTaskDepend> defTaskDependList) throws Exception {
		if (defTaskDependList == null || defTaskDependList.size() == 0) {
			return;
		}
		Set<Integer> preTaskIdSet = new HashSet<Integer>();
		for (DefTaskDepend defTaskDepend : defTaskDependList) {
			preTaskIdSet.add(defTaskDepend.getPreTaskId());
		}
		Set<Integer> lookupSet = new HashSet<Integer>();
		checkLoop(taskId, preTaskIdSet, lookupSet);
	}

	private void checkLoop(Integer taskId, Set<Integer> preTaskIdSet, Set<Integer> lookupSet) throws Exception {
		List<DefTaskDepend> postDefTaskDependList = defTaskDependService.getByPreId(taskId, 1);
		if (postDefTaskDependList == null || postDefTaskDependList.size() == 0) {
			return;
		}
		for (DefTaskDepend postDefTaskDepend : postDefTaskDependList) {
			if (preTaskIdSet.contains(postDefTaskDepend.getTaskId())) {
				throw new Exception("depend loop!");
			}
			if (lookupSet.contains(postDefTaskDepend.getTaskId())) {
				continue;
			}
			lookupSet.add(postDefTaskDepend.getTaskId());
			checkLoop(postDefTaskDepend.getTaskId(), preTaskIdSet, lookupSet);
		}
	}

	@Override
	public void removeById(int taskId) throws Exception {
		defTaskService.removeById(taskId);
		defTaskDependService.removeByTaskId(taskId);
		defObjectDependService.removeByTaskId(taskId);
		defMonitorBeginService.removeByTaskId(taskId);
		defMonitorDurService.removeByTaskId(taskId);
		defMonitorRetryService.removeByTaskId(taskId);
		defClassifyService.removeByTaskId(taskId);

		if (isProd) {
			String url = LeaderFinder.leaderURL + "/invalidInit.do?taskId=" + taskId;
			HttpClientUtils2.get(url);
		}
	}

	@Override
	public BODefTaskAllInfo getById(int taskId) throws Exception {
		DefTask defTask = defTaskService.getById(taskId);
		AbstractBODefTask boDefTask = ModelConverters.defPO2BO(defTask);

		int isValid = defTask.getIsValid();
		List<DefTaskDepend> defTaskDependList = defTaskDependService.getByPostId(taskId, isValid);
		List<DefObjectDepend> defObjectDependList = defObjectDependService.getByTaskId(taskId, isValid);
		List<DefMonitorBegin> defMonitorBeginList = defMonitorBeginService.getByTaskId(taskId, isValid);
		List<DefMonitorDur> defMonitorDurList = defMonitorDurService.getByTaskId(taskId, isValid);
		List<DefMonitorRetry> defMonitorRetryList = defMonitorRetryService.getByTaskId(taskId, isValid);
		List<DefClassify> defClassifyList = defClassifyService.getByTaskId(taskId, isValid);
		BODefTaskAllInfo boDefTaskAllInfo = new BODefTaskAllInfo(boDefTask, defTaskDependList, defObjectDependList, defMonitorBeginList, defMonitorDurList, defMonitorRetryList, defClassifyList);
		return boDefTaskAllInfo;
	}

	@Override
	public void offlineById(int taskId) throws Exception {
		offlineByIdServer(taskId);
		if (isProd) {
			String url = LeaderFinder.leaderURL + "/invalidInit.do?taskId=" + taskId;
			HttpClientUtils2.get(url);
		}
	}
	
	@Override
	public void offlineByIdServer(int taskId) {
		defTaskService.updateIsValidById(taskId, 0);
		defObjectDependService.updateIsValidById(taskId, 0);
		defMonitorBeginService.updateIsValidById(taskId, 0);
		defMonitorDurService.updateIsValidById(taskId, 0);
		defMonitorRetryService.updateIsValidById(taskId, 0);
		defClassifyService.updateIsValidById(taskId, 0);
	}
	
	@Override
	public void onlineById(int taskId) throws Exception {
		defTaskService.updateIsValidById(taskId, 1);
		defObjectDependService.updateIsValidById(taskId, 1);
		defMonitorBeginService.updateIsValidById(taskId, 1);
		defMonitorDurService.updateIsValidById(taskId, 1);
		defMonitorRetryService.updateIsValidById(taskId, 1);
		defClassifyService.updateIsValidById(taskId, 1);
	}

}
