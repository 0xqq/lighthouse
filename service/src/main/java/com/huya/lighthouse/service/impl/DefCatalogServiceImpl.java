package com.huya.lighthouse.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefCatalogDao;
import com.huya.lighthouse.model.po.def.DefCatalog;
import com.huya.lighthouse.model.po.def.DefTaskParam;
import com.huya.lighthouse.model.query.DefCatalogQuery;
import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.service.DefCatalogService;
import com.huya.lighthouse.service.DefTaskParamService;

/**
 * [DefCatalog] 的业务操作实现类
 * 
 */
@Service("defCatalogService")
@Transactional(rollbackFor = Throwable.class)
public class DefCatalogServiceImpl implements DefCatalogService {

	protected static final Logger log = LoggerFactory.getLogger(DefCatalogServiceImpl.class);

	@Autowired
	private DefCatalogDao defCatalogDao;
	@Autowired
	private DefTaskParamService defTaskParamService;

	/**
	 * 创建DefCatalog
	 **/
	public DefCatalog create(DefCatalog defCatalog) {
		Assert.notNull(defCatalog, "'defCatalog' must be not null");
		defCatalog.setCatalogId(null);
		defCatalogDao.insert(defCatalog);
		
		try {
			DefTaskParam defTaskParam = new DefTaskParam();
			defTaskParam.setCatalogId(defCatalog.getCatalogId());
			defTaskParam.setParamCode("hiveCmd");
			defTaskParam.setParamValue("hive");
			defTaskParam.setParamType("system");
			defTaskParam.setIsPassword(0);
			defTaskParam.setRemarks("");
			defTaskParam.setIsValid(1);
			defTaskParam.setCreateUser("system");
			defTaskParam.setUpdateUser("system");
			defTaskParam.setUpdateTime(new Date());
			defTaskParam.setCreateTime(new Date());
			defTaskParamService.create(defTaskParam);
			
			defTaskParam = new DefTaskParam();
			defTaskParam.setCatalogId(defCatalog.getCatalogId());
			defTaskParam.setParamCode("sparkCmd");
			defTaskParam.setParamValue("spark");
			defTaskParam.setParamType("system");
			defTaskParam.setIsPassword(0);
			defTaskParam.setRemarks("");
			defTaskParam.setIsValid(1);
			defTaskParam.setCreateUser("system");
			defTaskParam.setUpdateUser("system");
			defTaskParam.setUpdateTime(new Date());
			defTaskParam.setCreateTime(new Date());
			defTaskParamService.create(defTaskParam);
			
			defTaskParam = new DefTaskParam();
			defTaskParam.setCatalogId(defCatalog.getCatalogId());
			defTaskParam.setParamCode("hadoopQueue");
			defTaskParam.setParamValue("gamelivelimit");
			defTaskParam.setParamType("system");
			defTaskParam.setIsPassword(0);
			defTaskParam.setRemarks("");
			defTaskParam.setIsValid(1);
			defTaskParam.setCreateUser("system");
			defTaskParam.setUpdateUser("system");
			defTaskParam.setUpdateTime(new Date());
			defTaskParam.setCreateTime(new Date());
			defTaskParamService.create(defTaskParam);
			
			defTaskParam = new DefTaskParam();
			defTaskParam.setCatalogId(defCatalog.getCatalogId());
			defTaskParam.setParamCode("local_data_root");
			defTaskParam.setParamValue("/data/apps/lighthouse/instances/analyze-result/" + defCatalog.getCatalogId() + "/");
			defTaskParam.setParamType("system");
			defTaskParam.setIsPassword(0);
			defTaskParam.setRemarks("");
			defTaskParam.setIsValid(1);
			defTaskParam.setCreateUser("system");
			defTaskParam.setUpdateUser("system");
			defTaskParam.setUpdateTime(new Date());
			defTaskParam.setCreateTime(new Date());
			defTaskParamService.create(defTaskParam);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return defCatalog;
	}

	/**
	 * 更新DefCatalog
	 **/
	public DefCatalog update(DefCatalog defCatalog) {
		Assert.notNull(defCatalog, "'defCatalog' must be not null");
		defCatalogDao.update(defCatalog);
		return defCatalog;
	}

	/**
	 * 删除DefCatalog
	 **/
	public void removeById(int catalogId) {
		defCatalogDao.deleteById(catalogId);
	}

	/**
	 * 根据ID得到DefCatalog
	 **/
	public DefCatalog getById(int catalogId) {
		return defCatalogDao.getById(catalogId);
	}

	/**
	 * 分页查询: DefCatalog
	 **/
	@Transactional(readOnly = true)
	public Page<DefCatalog> findPage(DefCatalogQuery query) {
		Assert.notNull(query, "'query' must be not null");
		return defCatalogDao.findPage(query);
	}

	@Override
	public List<DefCatalog> getAllValid() {
		return defCatalogDao.getAllValid();
	}
}
