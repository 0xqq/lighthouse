package com.huya.lighthouse.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefTaskParamDao;
import com.huya.lighthouse.model.po.def.DefTaskParam;
import com.huya.lighthouse.model.query.DefTaskParamQuery;
import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.service.DefTaskParamService;
import com.huya.lighthouse.service.util.LeaderFinder;
import com.huya.lighthouse.util.HttpClientUtils2;

/**
 * [DefTaskParam] 的业务操作实现类
 * 
 */
@Service("defTaskParamService")
public class DefTaskParamServiceImpl implements DefTaskParamService {

	protected static final Logger log = LoggerFactory.getLogger(DefTaskParamServiceImpl.class);

	@Autowired
	private DefTaskParamDao defTaskParamDao;
	private boolean isProd = StringUtils.equals(System.getenv("DWENV"), "prod");

	private void notifyServer() throws Exception {
		if (isProd) {
			String url = LeaderFinder.leaderURL + "/reBuildDefParamFactory.do";
			HttpClientUtils2.get(url);
		}
	}

	/**
	 * 创建DefTaskParam
	 **/
	public DefTaskParam create(DefTaskParam defTaskParam) throws Exception {
		Assert.notNull(defTaskParam, "'defTaskParam' must be not null");
		defTaskParamDao.insert(defTaskParam);
		notifyServer();
		return defTaskParam;
	}

	/**
	 * 更新DefTaskParam
	 **/
	public DefTaskParam update(DefTaskParam defTaskParam) throws Exception {
		Assert.notNull(defTaskParam, "'defTaskParam' must be not null");
		defTaskParamDao.update(defTaskParam);
		notifyServer();
		return defTaskParam;
	}

	/**
	 * 删除DefTaskParam
	 **/
	public void removeById(Integer catalogId, String paramCode) throws Exception {
		defTaskParamDao.deleteById(catalogId, paramCode);
		notifyServer();
	}

	/**
	 * 根据ID得到DefTaskParam
	 **/
	public DefTaskParam getById(Integer catalogId, String paramCode) {
		return defTaskParamDao.getById(catalogId, paramCode);
	}

	@Override
	public List<DefTaskParam> getAllValid() {
		return defTaskParamDao.getAllValid();
	}

	/**
	 * 分页查询: DefTaskParam
	 **/
	public Page<DefTaskParam> findPage(DefTaskParamQuery query) {
		Assert.notNull(query, "'query' must be not null");
		return defTaskParamDao.findPage(query);
	}
}
