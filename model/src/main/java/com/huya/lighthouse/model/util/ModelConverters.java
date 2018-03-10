package com.huya.lighthouse.model.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.huya.lighthouse.model.bo.def.AbstractBODefTask;
import com.huya.lighthouse.model.bo.instance.BOInstanceTask;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.util.JsonUtils;

public class ModelConverters {

	public static AbstractBODefTask defPO2BO(DefTask defTask) throws Exception {
		if (defTask == null) {
			throw new Exception("poDefTask is null");
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> taskBodyMap = JsonUtils.decode(defTask.getTaskBody(), Map.class);

		Class<?> cla = Class.forName((String) taskBodyMap.get("class"));
		AbstractBODefTask boDefTask = (AbstractBODefTask) cla.newInstance();

		@SuppressWarnings("unchecked")
		Map<String, Object> poToMap = PropertyUtils.describe(defTask);
		poToMap.remove("taskBody");
		poToMap.putAll(taskBodyMap);

		boDefTask.registerConverter();
		BeanUtils.populate(boDefTask, poToMap);

		return boDefTask;
	}
	
	public static AbstractBODefTask str2BO(String taskBody) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> taskBodyMap = JsonUtils.decode(taskBody, Map.class);

		Class<?> cla = Class.forName((String) taskBodyMap.get("class"));
		AbstractBODefTask boDefTask = (AbstractBODefTask) cla.newInstance();
		
		boDefTask.registerConverter();
		BeanUtils.populate(boDefTask, taskBodyMap);

		return boDefTask;
	}

	public static DefTask defBO2PO(AbstractBODefTask boDefTask) throws Exception {
		if (boDefTask == null) {
			throw new Exception("boDefTask is null");
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> boToMap = PropertyUtils.describe(boDefTask);

		Map<String, Object> baseBOValMap = new HashMap<String, Object>();
		Field[] fields = AbstractBODefTask.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			Object val = boToMap.remove(fieldName);
			baseBOValMap.put(fieldName, val);
		}

		String taskBody = JsonUtils.encode(boToMap);
		baseBOValMap.put("taskBody", taskBody);

		DefTask defTask = new DefTask();
		BeanUtils.populate(defTask, baseBOValMap);
		return defTask;
	}

	public static BOInstanceTask instancePO2BO(InstanceTask instanceTask) throws Exception {
		if (instanceTask == null) {
			throw new Exception("instanceTask is null");
		}
		AbstractBODefTask boDefTask = str2BO(instanceTask.getTaskBody());
		@SuppressWarnings("unchecked")
		Map<String, Object> poToMap = PropertyUtils.describe(instanceTask);
		poToMap.remove("taskBody");
		BeanUtils.populate(boDefTask, poToMap);
		
		BOInstanceTask boInstanceTask = new BOInstanceTask();
		BeanUtils.populate(boInstanceTask, poToMap);
		
		boInstanceTask.setBoDefTask(boDefTask);
		return boInstanceTask;
	}
}
