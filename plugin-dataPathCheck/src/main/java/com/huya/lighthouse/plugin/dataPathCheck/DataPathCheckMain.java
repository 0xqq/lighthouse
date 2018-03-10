package com.huya.lighthouse.plugin.dataPathCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.DefTaskDataPathCheck;
import com.huya.lighthouse.plugin.AbstractPlugin;

public class DataPathCheckMain extends AbstractPlugin {

	protected static Logger logger = LoggerFactory.getLogger(DataPathCheckMain.class);

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		DefTaskDataPathCheck defTaskDataPathCheck = (DefTaskDataPathCheck) readObject();
		FileCheck.check(defTaskDataPathCheck);
	}

}
