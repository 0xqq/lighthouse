package com.huya.lighthouse.plugin.jdbc2hive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.dataswitch.input.JdbcInput;
import com.huya.lighthouse.dataswitch.output.FileOutput;
import com.huya.lighthouse.dataswitch.serializer.TxtSerializerExt;
import com.huya.lighthouse.dataswitch.util.InputOutputUtil;
import com.huya.lighthouse.model.bo.def.DefTaskDataPathCheck;
import com.huya.lighthouse.model.bo.def.DefTaskJdbc2hive;
import com.huya.lighthouse.model.bo.def.FromJdbcSep;
import com.huya.lighthouse.plugin.AbstractPlugin;
import com.huya.lighthouse.plugin.dataPathCheck.FileCheck;
import com.huya.lighthouse.util.ShellExecUtils;

public class Jdbc2hiveMain extends AbstractPlugin {

	private static Logger logger = LoggerFactory.getLogger(Jdbc2hiveMain.class);

	private static DefTaskJdbc2hive defTaskJdbc2hive = null;
	private static ExecutorService executorService = null;

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		defTaskJdbc2hive = (DefTaskJdbc2hive) readObject();
		exec();
	}

	private static void exec() throws Exception {
		mkDataDir();
		try {
			logger.info("\n############################begin jdbc to file############################\n");
			executorService = Executors.newFixedThreadPool(defTaskJdbc2hive.getFromJdbcSepList().size() > 5 ? 5: defTaskJdbc2hive.getFromJdbcSepList().size());
			jdbcToFile();
			logger.info("\n############################end jdbc to file############################\n\n");

			FileCheck.check(new DefTaskDataPathCheck(getDataDir(), 0, null, defTaskJdbc2hive.getSumFileRow()));

			logger.info("\n############################begin file to hive############################\n");
			fileToHive();
			logger.info("\n############################end file to hive############################\n\n");
		} finally {
			ShellExecUtils.exec("rm -fr " + getDataDir());
			if (executorService != null) {
				executorService.shutdown();
			}
		}
	}

	private static void fileToHive() throws Exception {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(defTaskJdbc2hive.getHiveCmd()).append(" -e \"");
		cmdBuilder.append("set mapred.job.priority=HIGH;set mapred.job.queue.name=").append(defTaskJdbc2hive.getHadoopQueue()).append(";");
		cmdBuilder.append("LOAD DATA LOCAL INPATH '").append(getDataDir()).append("/*' OVERWRITE INTO TABLE ").append(defTaskJdbc2hive.getHiveDBDotTable());
		if (StringUtils.isNotBlank(defTaskJdbc2hive.getPartition())) {
			cmdBuilder.append(" PARTITION (").append(defTaskJdbc2hive.getPartition()).append(")");
		}
		cmdBuilder.append("\"");
		ShellExecUtils.exec(cmdBuilder.toString());
	}

	private static void jdbcToFile() throws Exception {
		List<FromJdbcSep> fromJdbcSepList = defTaskJdbc2hive.getFromJdbcSepList();
		if (fromJdbcSepList == null || fromJdbcSepList.size() == 0) {
			throw new Exception("fromJdbcSepList cannot be null");
		}
		List<Future<String>> futureList = new ArrayList<Future<String>>();
		for (final FromJdbcSep fromJdbcSep : fromJdbcSepList) {
			Future<String> future = executorService.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					TxtSerializerExt serializer = new TxtSerializerExt();
					FileOutput fileOutput = new FileOutput();
					String targetDataDir = getDataDir() + "/" + UUID.randomUUID().toString().replaceAll("-", "");
					File targetDataDirFile = new File(targetDataDir);
					targetDataDirFile.mkdirs();
					fileOutput.setDir(targetDataDir);
					fileOutput.setSerializer(serializer);
					try {
						if (fromJdbcSep.getEndIndex() > 0 && fromJdbcSep.getEndIndex() > fromJdbcSep.getStartIndex()) {
							for (int i = fromJdbcSep.getStartIndex(); i <= fromJdbcSep.getEndIndex(); i++) {
								JdbcInput jdbcInput = new JdbcInput();
								jdbcInput.setDriverClass(fromJdbcSep.getJdbcDriver());
								jdbcInput.setUrl(fromJdbcSep.getJdbcUrl());
								jdbcInput.setUsername(fromJdbcSep.getJdbcUsername());
								jdbcInput.setPassword(fromJdbcSep.getJdbcPassword());
								String sql = StringUtils.replace(fromJdbcSep.getSql(), "#index#", i + "");
								jdbcInput.setSql(sql);
								try {
									InputOutputUtil.copy(jdbcInput, fileOutput);
								} finally {
									IOUtils.closeQuietly(jdbcInput);
								}
							}
						} else {
							JdbcInput jdbcInput = new JdbcInput();
							jdbcInput.setDriverClass(fromJdbcSep.getJdbcDriver());
							jdbcInput.setUrl(fromJdbcSep.getJdbcUrl());
							jdbcInput.setUsername(fromJdbcSep.getJdbcUsername());
							jdbcInput.setPassword(fromJdbcSep.getJdbcPassword());
							jdbcInput.setSql(fromJdbcSep.getSql());
							try {
								InputOutputUtil.copy(jdbcInput, fileOutput);
							} finally {
								IOUtils.closeQuietly(jdbcInput);
							}
						}
						File sourceFile = new File(targetDataDir + "/00000");
						if (!sourceFile.exists()) {
							sourceFile.createNewFile();
						}
						File destFile = new File(getDataDir() + "/" + UUID.randomUUID().toString().replaceAll("-", ""));
						FileUtils.moveFile(sourceFile, destFile);
						FileUtils.deleteDirectory(targetDataDirFile);
					} finally {
						IOUtils.closeQuietly(fileOutput);
					}
					return "";
				}
			});
			futureList.add(future);
		}
		
		for (Future<String> future : futureList) {
			future.get();
		}
	}

	private static void mkDataDir() {
		String dataDir = getDataDir();
		new File(dataDir).mkdirs();
	}

	private static String getDataDir() {
		return instanceDir + "/data";
	}
}
