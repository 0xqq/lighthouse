package com.huya.lighthouse.plugin.toRsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.dataswitch.util.HdfsUtils2;
import com.huya.lighthouse.model.bo.def.DefTaskToRsync;
import com.huya.lighthouse.plugin.AbstractPlugin;
import com.huya.lighthouse.util.ShellExecUtils;

public class ToRsyncMain extends AbstractPlugin {

	protected static Logger logger = LoggerFactory.getLogger(ToRsyncMain.class);
	private static DefTaskToRsync defTaskToRsync = null;

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		defTaskToRsync = (DefTaskToRsync) readObject();
		exec();
	}

	private static void exec() throws Exception {
		String rsyncPath = defTaskToRsync.getDataPaths();
		if (StringUtils.isBlank(rsyncPath) || StringUtils.startsWith(rsyncPath, "hdfs://")) {
			hdfs2local(instanceDir, defTaskToRsync.getDataPaths());
			rsyncPath = getDataDir();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("#!/bin/bash \n");
		sb.append("source /etc/profile \n");
		sb.append("export RSYNC_PASSWORD='").append(defTaskToRsync.getRsyncPassword()).append("'\n");
		sb.append("rsync -rvz --delete --progress --port=").append(defTaskToRsync.getRsyncPort()).append(" ");
		sb.append(rsyncPath).append(" ").append(defTaskToRsync.getRsyncUserName()).append("@").append(defTaskToRsync.getRsyncIp()).append("::");
		sb.append(defTaskToRsync.getRsyncModuleName());
		if (StringUtils.isNotBlank(defTaskToRsync.getRsyncPathUnderModule())) {
			sb.append("/").append(defTaskToRsync.getRsyncPathUnderModule());
		}
		logger.info("\n============rsync command============\n" + sb.toString() + "=====================================\n");

		String rsyncFile = getRsyncPath();
		FileUtils.writeStringToFile(new File(rsyncFile), sb.toString());

		ShellExecUtils.exec("/bin/bash " + rsyncFile);
	}

	private static void hdfs2local(String instanceDir, String dataPaths) throws Exception {
		List<String> dirs = new ArrayList<String>();
		if (StringUtils.isNotBlank(dataPaths) && StringUtils.startsWithIgnoreCase(dataPaths, "hdfs://")) {
			String[] dataPathArr = StringUtils.split(dataPaths, ",");
			if (dataPaths != null) {
				for (String dataPath : dataPathArr) {
					dirs.add(StringUtils.trim(dataPath));
				}
			}
		} else {
			dirs = getOutputPaths();
		}
		if (dirs == null || dirs.size() == 0) {
			throw new Exception("input dir cannot be empty!");
		}

		mkDataDir();
		HdfsUtils2.copyHdfs2local(dirs, getDataDir());
	}

	private static void mkDataDir() {
		String dataDir = getDataDir();
		new File(dataDir).mkdirs();
	}

	private static String getDataDir() {
		return instanceDir + "/data/";
	}

	private static String getRsyncPath() {
		return instanceDir + "/rsync.sh";
	}
}
