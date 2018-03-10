package com.huya.lighthouse.server.factory;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huya.lighthouse.model.po.def.DefLessee;

public class DefLesseeFactory {

	protected static Logger logger = LoggerFactory.getLogger(DefLesseeFactory.class);

	private static Cache<Integer, DefLessee> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(2, TimeUnit.MINUTES).build();

	public static DefLessee getByCatalogId(Integer catalogId) {
		DefLessee defLessee = cache.getIfPresent(catalogId);
		if (defLessee == null) {
			defLessee = ServiceBeanFactory.getDefLesseeService().getByCatalogId(catalogId);
			if (defLessee != null) {
				cache.put(catalogId, defLessee);
			}
		}
		return defLessee;
	}
}
