package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefQueueDao;
import com.huya.lighthouse.model.po.def.DefQueue;
import com.huya.lighthouse.model.query.DefQueueQuery;
import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.service.DefQueueService;
import com.huya.lighthouse.service.util.LeaderFinder;
import com.huya.lighthouse.util.HttpClientUtils2;

/**
 * [DefQueue] 的业务操作实现类
 * 
 */
@Service("defQueueService")
public class DefQueueServiceImpl implements DefQueueService {

	protected static final Logger log = LoggerFactory.getLogger(DefQueueServiceImpl.class);

	@Autowired
	private DefQueueDao defQueueDao;

	/**
	 * 创建DefQueue
	 **/
	public DefQueue create(DefQueue defQueue) {
		Assert.notNull(defQueue, "'defQueue' must be not null");
		defQueueDao.insert(defQueue);
		return defQueue;
	}

	/**
	 * 更新DefQueue
	 **/
	public DefQueue update(DefQueue defQueue) throws Exception {
		Assert.notNull(defQueue, "'defQueue' must be not null");
		defQueueDao.update(defQueue);
		String url = LeaderFinder.leaderURL + "/updateQueueSize.do?queueId=" + defQueue.getQueueId() + "&queueSize=" + defQueue.getQueueSize();
		HttpClientUtils2.get(url);
		return defQueue;
	}

	/**
	 * 删除DefQueue
	 **/
	public void removeById(int queueId) {
		defQueueDao.deleteById(queueId);
	}

	/**
	 * 根据ID得到DefQueue
	 **/
	public DefQueue getById(int queueId) {
		return defQueueDao.getById(queueId);
	}

	@Override
	public Page<DefQueue> findPage(DefQueueQuery query) {
		return defQueueDao.findPage(query);
	}

	@Override
	public List<DefQueue> getAllValid() {
		return defQueueDao.getAllValid();
	}

}
