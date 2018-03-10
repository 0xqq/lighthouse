package com.huya.lighthouse.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huya.lighthouse.model.po.instance.InstanceTaskKey;
import com.huya.lighthouse.service.TaskActionService;
import com.huya.lighthouse.service.util.LeaderFinder;
import com.huya.lighthouse.util.HttpClientUtils2;
import com.huya.lighthouse.util.JsonUtils;

/**
 * 
 * 
 */
@Service("taskActionService")
public class TaskActionServiceImpl implements TaskActionService {

	protected static final Logger log = LoggerFactory.getLogger(TaskActionServiceImpl.class);

	@Override
	public void run(String startDate, String endDate, String taskIds, Integer isSelfRun, Integer isForceRun) throws Exception {
		String url = LeaderFinder.leaderURL + "/run.do?startDate=" + startDate + "&endDate=" + endDate + "&taskIds=" + taskIds + "&isSelfRun=" + isSelfRun + "&isForceRun=" + isForceRun;
		HttpClientUtils2.get(url);
	}

	@Override
	public void kill(List<InstanceTaskKey> instanceTaskKeyList) throws Exception {
		if (instanceTaskKeyList == null || instanceTaskKeyList.size() == 0) {
			return;
		}
		String url = LeaderFinder.leaderURL + "/kill.do";
		Map<String, String> paramMap = new HashMap<String, String>();
		String jsonStr = JsonUtils.encode(instanceTaskKeyList);
		paramMap.put("listStr", jsonStr);
		log.info("kill: " + jsonStr);
		HttpClientUtils2.postParameters(url, paramMap);
	}

	@Override
	public void setSuccess(List<InstanceTaskKey> instanceTaskKeyList) throws Exception {
		if (instanceTaskKeyList == null || instanceTaskKeyList.size() == 0) {
			return;
		}
		String url = LeaderFinder.leaderURL + "/setSuccess.do";
		Map<String, String> paramMap = new HashMap<String, String>();
		String jsonStr = JsonUtils.encode(instanceTaskKeyList);
		paramMap.put("listStr", jsonStr);
		log.info("setSuccess: " + jsonStr);
		HttpClientUtils2.postParameters(url, paramMap);
	}
}
