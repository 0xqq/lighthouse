package com.huya.lighthouse.model.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.DefTaskJdbc2hive;
import com.huya.lighthouse.model.bo.def.DefTaskShell;
import com.huya.lighthouse.model.bo.def.FromJdbcSep;
import com.huya.lighthouse.model.po.def.DefTask;

public class ModelConvertersTest {

	private static Logger logger = LoggerFactory.getLogger(ModelConvertersTest.class);
	
	@Test
	public void test1() throws Exception {
		DefTaskShell defTaskShell = new DefTaskShell();
		defTaskShell.setTaskId(99);
		defTaskShell.setCatalogId(100);
		defTaskShell.setTaskName("Test");
		defTaskShell.setTaskPlugin("java -Xmx 1024m xxxx");
		defTaskShell.setExecCronExp("0 0 * * * ? *");
		defTaskShell.setAgentHostGroup("hive.agent.lighthouse.huya.com");
		defTaskShell.setQueueId(97);
		defTaskShell.setPriority(10);
		defTaskShell.setMaxRunNum(10);
		defTaskShell.setMaxRunSec(24 * 60 * 60);
		defTaskShell.setMaxRetryNum(2);
		defTaskShell.setRetryInterval(120);
		defTaskShell.setIsIgnoreError(0);
		defTaskShell.setOfflineTime(DateUtils.parseDate("2017-03-01", new String[] {"yyyy-MM-dd"}));
		defTaskShell.setIsValid(1);
		defTaskShell.setCreateTime(DateUtils.parseDate("2017-01-01", new String[] {"yyyy-MM-dd"}));
		defTaskShell.setUpdateTime(DateUtils.parseDate("2017-02-01", new String[] {"yyyy-MM-dd"}));
		defTaskShell.setCreateUser("dw_chenwu");
		defTaskShell.setUpdateUser("dw_chenwu");
		
		defTaskShell.setLinuxRunUser("root");
		defTaskShell.setRemarks("remarks xxxxx");
		
		defTaskShell.setScript("set mapred.job.queue.name=zhgame;\r\n select * from dim_product limit 10\r\n");
		
		DefTask poDefTask = ModelConverters.defBO2PO(defTaskShell);
		logger.info(poDefTask.toString());
		
		DefTaskShell boDefTask = (DefTaskShell) ModelConverters.defPO2BO(poDefTask);
		
		logger.info(boDefTask.toString());
	}
	
	@Test
	public void test2() throws Exception {
		String dateStr = DateFormatUtils.format(new Date(), "dow mon dd HH:mm:ss zzz yyyy");
		logger.info(dateStr);
		logger.info(new Date().toString());
	}
	
	@Test
	public void testJdbc2Hive() throws Exception {
		DefTaskJdbc2hive defTaskJdbc2hive = new DefTaskJdbc2hive();
		defTaskJdbc2hive.setTaskId(99);
		defTaskJdbc2hive.setCatalogId(100);
		defTaskJdbc2hive.setTaskName("Test");
		defTaskJdbc2hive.setTaskPlugin("java -Xmx 1024m xxxx");
		defTaskJdbc2hive.setExecCronExp("0 0 * * * ? *");
		defTaskJdbc2hive.setAgentHostGroup("hive.agent.lighthouse.huya.com");
		defTaskJdbc2hive.setQueueId(97);
		defTaskJdbc2hive.setPriority(10);
		defTaskJdbc2hive.setMaxRunNum(10);
		defTaskJdbc2hive.setMaxRunSec(24 * 60 * 60);
		defTaskJdbc2hive.setMaxRetryNum(2);
		defTaskJdbc2hive.setRetryInterval(120);
		defTaskJdbc2hive.setIsIgnoreError(0);
		defTaskJdbc2hive.setOfflineTime(DateUtils.parseDate("2017-03-01", new String[] {"yyyy-MM-dd"}));
		defTaskJdbc2hive.setIsValid(1);
		defTaskJdbc2hive.setCreateTime(DateUtils.parseDate("2017-01-01", new String[] {"yyyy-MM-dd"}));
		defTaskJdbc2hive.setUpdateTime(DateUtils.parseDate("2017-02-01", new String[] {"yyyy-MM-dd"}));
		defTaskJdbc2hive.setCreateUser("dw_chenwu");
		defTaskJdbc2hive.setUpdateUser("dw_chenwu");
		
		defTaskJdbc2hive.setLinuxRunUser("root");
		defTaskJdbc2hive.setRemarks("remarks xxxxx");
		
		defTaskJdbc2hive.setHiveDBDotTable("zhgame.ods_acion_log");
		defTaskJdbc2hive.setPartition(null);
		defTaskJdbc2hive.getFromJdbcSepList().add(new FromJdbcSep("mysql", "url", "username", "password", "sql", 0, 10));
		defTaskJdbc2hive.getFromJdbcSepList().add(new FromJdbcSep("mysql1", "url1", "username1", "password1", "sql1", 11, 20));
		defTaskJdbc2hive.getFromJdbcSepList().add(new FromJdbcSep("mysql2", "url2", "username2", "password2", "sql2", 21, 30));
		
		DefTask poDefTask = ModelConverters.defBO2PO(defTaskJdbc2hive);
		logger.info(poDefTask.toString());
		
		DefTaskJdbc2hive boDefTask = (DefTaskJdbc2hive) ModelConverters.defPO2BO(poDefTask);
		
		logger.info(boDefTask.toString());
	}
	
	@Test
	public void test3() throws Exception {
		Map<String, Object> poToMap = new HashMap<String, Object>();
		poToMap.put("abc", "abc");
		poToMap.put("taskId", 10);
		DefTaskJdbc2hive boDefTask = new DefTaskJdbc2hive();
		BeanUtils.populate(boDefTask, poToMap);
		logger.info(boDefTask.toString());
	}
}
