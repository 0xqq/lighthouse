package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefLesseeDao;
import com.huya.lighthouse.model.po.def.DefLessee;
import com.huya.lighthouse.service.DefLesseeService;

/**
 * [DefLessee] 的业务操作实现类
 * 
 */
@Service("defLesseeService")
@Transactional(rollbackFor = Throwable.class)
public class DefLesseeServiceImpl implements DefLesseeService {

	protected static final Logger log = LoggerFactory.getLogger(DefLesseeServiceImpl.class);

	@Autowired
	private DefLesseeDao defLesseeDao;

	/**
	 * 创建DefLessee
	 **/
	public DefLessee create(DefLessee defLessee) {
		Assert.notNull(defLessee, "'defLessee' must be not null");
		defLesseeDao.insert(defLessee);
		return defLessee;
	}

	/**
	 * 更新DefLessee
	 **/
	public DefLessee update(DefLessee defLessee) {
		Assert.notNull(defLessee, "'defLessee' must be not null");
		defLesseeDao.update(defLessee);
		return defLessee;
	}

	/**
	 * 删除DefLessee
	 **/
	public void removeById(int lesseeId) {
		defLesseeDao.deleteById(lesseeId);
	}

	/**
	 * 根据ID得到DefLessee
	 **/
	public DefLessee getById(int lesseeId) {
		return defLesseeDao.getById(lesseeId);
	}

	@Override
	public List<DefLessee> getAllValid() {
		return defLesseeDao.getAllValid();
	}

	@Override
	public DefLessee getByCatalogId(Integer catalogId) {
		return defLesseeDao.getByCatalogId(catalogId);
	}
}
