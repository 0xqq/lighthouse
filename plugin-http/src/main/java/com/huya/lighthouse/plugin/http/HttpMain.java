package com.huya.lighthouse.plugin.http;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.bo.def.DefTaskHttp;
import com.huya.lighthouse.plugin.AbstractPlugin;
import com.huya.lighthouse.util.HttpClientUtils2;

public class HttpMain extends AbstractPlugin {

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		DefTaskHttp defTaskHttp = (DefTaskHttp) readObject();
		String result = HttpClientUtils2.get(defTaskHttp.getUrl());
		if (StringUtils.equals(result, "200") || StringUtils.equals(result, "0")) {
			return;
		} else {
			throw new Exception("result != 200 or 0");
		}
	}

}
