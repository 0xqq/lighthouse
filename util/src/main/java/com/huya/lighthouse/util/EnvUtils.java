package com.huya.lighthouse.util;

import java.util.Arrays;
import java.util.List;

public class EnvUtils {

	private static String envType = null;

	public static String getEnvType() {
		if (envType == null) {
			String envTypeTmp = System.getenv("DWENV");
			List<String> standEnvTypes = Arrays.asList(new String[] { "prod", "test", "dev" });
			if (!standEnvTypes.contains(envTypeTmp)) {
				envTypeTmp = "dev";
			}
			envType = envTypeTmp;
		}
		return envType;
	}
}
