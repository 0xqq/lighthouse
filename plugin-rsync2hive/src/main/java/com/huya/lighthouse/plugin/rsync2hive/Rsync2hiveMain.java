package com.huya.lighthouse.plugin.rsync2hive;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.DefTaskDataPathCheck;
import com.huya.lighthouse.model.bo.def.DefTaskRsync2hive;
import com.huya.lighthouse.plugin.AbstractPlugin;
import com.huya.lighthouse.plugin.dataPathCheck.FileCheck;
import com.huya.lighthouse.util.ShellExecUtils;

public class Rsync2hiveMain extends AbstractPlugin {

	private static Logger logger = LoggerFactory.getLogger(Rsync2hiveMain.class);

	private static DefTaskRsync2hive defTaskRsync2hive = null;

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		defTaskRsync2hive = (DefTaskRsync2hive) readObject();
		exec();
	}

	private static void exec() throws Exception {
		mkDataDir();
		try {
			logger.info("\n############################begin rsync############################\n");
			rsyncToFile();
			logger.info("\n############################end rsync############################\n\n");
	
			FileCheck.check(new DefTaskDataPathCheck(getDataDir(), defTaskRsync2hive.getFileCount(), defTaskRsync2hive.getSumFileSize(), defTaskRsync2hive.getSumFileRow()));
	
			logger.info("\n############################begin file to hive############################\n");
			fileToHive();
			logger.info("\n############################end file to hive############################\n\n");
		} finally {
			ShellExecUtils.exec("rm -fr " + getDataDir());
		}
	}

	private static void fileToHive() throws Exception {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(defTaskRsync2hive.getHiveCmd()).append(" -e \"");
		cmdBuilder.append("set mapred.job.priority=HIGH;set mapred.job.queue.name=").append(defTaskRsync2hive.getHadoopQueue()).append(";");
		cmdBuilder.append("LOAD DATA LOCAL INPATH '").append(getDataDir()).append("/*' OVERWRITE INTO TABLE ").append(defTaskRsync2hive.getHiveDBDotTable());
		if (StringUtils.isNotBlank(defTaskRsync2hive.getPartition())) {
			cmdBuilder.append(" PARTITION (").append(defTaskRsync2hive.getPartition()).append(")");
		}
		cmdBuilder.append("\"");
		ShellExecUtils.exec(cmdBuilder.toString());
	}

	private static void rsyncToFile() throws Exception {
		String rsyncFile = getRsyncPath();
		String dataDir = getDataDir();
		StringBuilder sb = new StringBuilder();
		sb.append("#!/bin/bash \n");
		sb.append("source /etc/profile \n");
		sb.append("export RSYNC_PASSWORD='").append(defTaskRsync2hive.getRsyncPassword()).append("'\n");
		sb.append("rsync -rvz --delete --progress --port=").append(defTaskRsync2hive.getRsyncPort()).append(" ");
		sb.append(defTaskRsync2hive.getRsyncUserName()).append("@").append(defTaskRsync2hive.getRsyncIp());
		sb.append("::").append(defTaskRsync2hive.getRsyncModuleName());
		if (StringUtils.isNotBlank(defTaskRsync2hive.getRsyncPathUnderModule())) {
			sb.append("/").append(defTaskRsync2hive.getRsyncPathUnderModule());
		}
		sb.append(" ").append(dataDir);
		logger.info("\n============rsync command============\n" + sb.toString() + "=====================================\n");

		FileUtils.writeStringToFile(new File(rsyncFile), sb.toString());
		ShellExecUtils.exec("/bin/bash " + rsyncFile);
	}

	private static void mkDataDir() {
		String dataDir = getDataDir();
		new File(dataDir).mkdirs();
	}

	private static String getDataDir() {
		return instanceDir + "/data";
	}

	private static String getRsyncPath() {
		return instanceDir + "/rsync.sh";
	}

}
