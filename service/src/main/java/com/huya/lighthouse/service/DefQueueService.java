package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefQueue;
import com.huya.lighthouse.model.query.DefQueueQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * [DefQueue] 的业务操作
 * 
 */
public interface DefQueueService {

	/**
	 * 创建DefQueue
	 **/
	public DefQueue create(DefQueue defQueue);

	/**
	 * 更新DefQueue
	 **/
	public DefQueue update(DefQueue defQueue) throws Exception;

	/**
	 * 删除DefQueue
	 **/
	public void removeById(int queueId);

	/**
	 * 根据ID得到DefQueue
	 **/
	public DefQueue getById(int queueId);

	/** 
	 * 分页查询: DefQueue
	 **/      
	public Page<DefQueue> findPage(DefQueueQuery query);

	public List<DefQueue> getAllValid();
}
