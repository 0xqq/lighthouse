package com.huya.lighthouse.plugin.hql;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.huya.lighthouse.model.bo.def.DefTaskHql;
import com.huya.lighthouse.plugin.AbstractPlugin;
import com.huya.lighthouse.util.ShellExecUtils;

public class HqlMain extends AbstractPlugin {

	private static DefTaskHql defTaskHql = null;

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		defTaskHql = (DefTaskHql) readObject();
		boolean isSuccess = exec();
		if (isSuccess) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

	private static boolean exec() throws Exception {
		createHqlFile();

		boolean isRunSpark = StringUtils.isNotBlank(defTaskHql.getSparkCmd());
		boolean isRunHive = StringUtils.isNotBlank(defTaskHql.getHiveCmd());
		if (!isRunSpark && isRunHive) {
			return runHive();
		} else if (isRunSpark && !isRunHive) {
			return runSpark(0);
		} else if (isRunSpark && isRunHive) {
			return runSparkAndHive();
		} else {
			throw new Exception("sparkCmd and hiveCmd is null");
		}
	}

	private static boolean runSparkAndHive() throws Exception {
		Integer sparkTimeOutSec = defTaskHql.getSparkTimeOutSec();
		if (sparkTimeOutSec == null || sparkTimeOutSec == 0) {
			sparkTimeOutSec = 600;
		}

		boolean isSuccess = runSpark(sparkTimeOutSec * 1l);
		if (isSuccess) {
			return true;
		}

		return runHive();
	}

	private static boolean runSpark(long timeoutMillis) {
		StringBuilder sparkBuilder = new StringBuilder();
		sparkBuilder.append(defTaskHql.getSparkCmd()).append(" -f ").append(getHqlPath());
		return ShellExecUtils.execQuietly(sparkBuilder.toString(), timeoutMillis);
	}

	private static boolean runHive() {
		StringBuilder hiveBuilder = new StringBuilder();
		hiveBuilder.append(defTaskHql.getHiveCmd()).append(" -f ").append(getHqlPath());
		return ShellExecUtils.execQuietly(hiveBuilder.toString());
	}

	private static void createHqlFile() throws Exception {
		StringBuilder hqlBuilder = new StringBuilder();
		String hql = doHql(defTaskHql.getSql());
		hqlBuilder.append("set mapred.job.priority=HIGH;\n");
		hqlBuilder.append("set mapred.job.queue.name=").append(defTaskHql.getHadoopQueue()).append(";\n");
		hqlBuilder.append(hql).append("\n");
		String hqlPath = getHqlPath();
		FileUtils.writeStringToFile(new File(hqlPath), hqlBuilder.toString());
	}

	private static String doHql(String hql) throws Exception {
		String[] hqlArr = StringUtils.split(hql, ";");
		if (hqlArr == null) {
			return null;
		}
		List<String> outputPaths = new ArrayList<String>();
		StringBuilder hqlBuilder = new StringBuilder();
		for (int i = 0; i < hqlArr.length; i++) {
			String subHql = StringUtils.trim(hqlArr[i]);
			if (StringUtils.startsWithIgnoreCase(subHql, "select ")) {
				String outputPath = getHdfsPath() + "/" + DateFormatUtils.format(getTaskDate(), "yyyyMMdd") + "/" + getCatalogId() + "/" + getTaskId() + "/"
						+ DateFormatUtils.format(getTaskDate(), "yyyyMMddHHmmss") + "/" + i;
				subHql = "INSERT OVERWRITE DIRECTORY '" + outputPath + "'\n" + subHql;
				outputPaths.add(outputPath);
			}
			hqlBuilder.append(subHql).append(";");
		}
		if (outputPaths.size() > 0) {
			contextMap.put(outputPathsKey, outputPaths);
			writeContextFile();
		}
		return hqlBuilder.toString();
	}

	private static String getHqlPath() {
		return instanceDir + "/hql.sql";
	}

	private static String getHdfsPath() {
		return "hdfs://huyacluster/tmp/lighthouse/analyse-result";
	}
}
