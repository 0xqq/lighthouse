package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefQueue;
import com.huya.lighthouse.model.query.DefQueueQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_queue [DefQueue] 的Dao操作
 * 
 */
public interface DefQueueDao {

	public void insert(DefQueue entity);

	public int update(DefQueue entity);

	public int deleteById(int queueId);

	public DefQueue getById(int queueId);

	public Page<DefQueue> findPage(DefQueueQuery query);

	public List<DefQueue> getAllValid();

}
