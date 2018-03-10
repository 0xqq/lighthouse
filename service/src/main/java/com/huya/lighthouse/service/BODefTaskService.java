package com.huya.lighthouse.service;

import com.huya.lighthouse.model.bo.def.BODefTaskAllInfo;

/**
 * 
 */
public interface BODefTaskService {

	public void create(BODefTaskAllInfo bODefTaskAllInfo) throws Exception;

	public void update(BODefTaskAllInfo bODefTaskAllInfo) throws Exception;

	public void removeById(int taskId) throws Exception;

	public BODefTaskAllInfo getById(int taskId) throws Exception;
	
	public void offlineById(int taskId) throws Exception;
	
	public void onlineById(int taskId) throws Exception;

	public void offlineByIdServer(int taskId);
	
	public void rebuildInit(int taskId) throws Exception;
}
