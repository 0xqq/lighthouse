package com.huya.lighthouse.server.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefTaskParam;

public class DefParamFactory {

	private static Logger logger = LoggerFactory.getLogger(DefParamFactory.class);
	private static Map<Integer, Map<String, String>> catalogParamMap = new HashMap<Integer, Map<String, String>>();
	
	public static synchronized void load() {
		long startTime = System.currentTimeMillis();
		Map<Integer, Map<String, String>> curCatalogParamMap = new HashMap<Integer, Map<String, String>>();
		List<DefTaskParam> defTaskParamList = ServiceBeanFactory.getDefTaskParamService().getAllValid();
		if (defTaskParamList == null) {
			logger.info("None DefTaskParam");
		}
		for (DefTaskParam defTaskParam : defTaskParamList) {
			Integer catalogId = defTaskParam.getCatalogId();
			Map<String, String> paramMap = curCatalogParamMap.get(catalogId);
			if (paramMap == null) {
				paramMap = new HashMap<String, String>();
				curCatalogParamMap.put(catalogId, paramMap);
			}
			paramMap.put(defTaskParam.getParamCode(), defTaskParam.getParamValue());
		}
		catalogParamMap = curCatalogParamMap;
		logger.info("=========================DefParamFactory load: " + (System.currentTimeMillis()-startTime));
	}
	
	public static Map<String, String> getByCatalogId(Integer catalogId) {
		return catalogParamMap.get(catalogId);
	}
}
