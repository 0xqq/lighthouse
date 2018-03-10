package com.huya.lighthouse.plugin.shell;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.huya.lighthouse.model.bo.def.DefTaskShell;
import com.huya.lighthouse.plugin.AbstractPlugin;
import com.huya.lighthouse.util.ShellExecUtils;

public class ShellMain extends AbstractPlugin {

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];

		DefTaskShell defTaskShell = (DefTaskShell) readObject();

		StringBuilder synContent = new StringBuilder();
		synContent.append("#!/bin/bash\n");
		synContent.append("source /etc/profile\n");
		synContent.append(defTaskShell.getScript()).append("\n");
		String scriptPath = getScriptPath(instanceDir);

		FileUtils.writeStringToFile(new File(scriptPath), synContent.toString());

		ShellExecUtils.exec("/bin/bash " + scriptPath);
	}

	private static String getScriptPath(String instanceDir) {
		return instanceDir + "/script.sh";
	}

}
