package com.huya.lighthouse.plugin.toJdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.dataswitch.input.FileInput;
import com.huya.lighthouse.dataswitch.input.TxtFileInput;
import com.huya.lighthouse.dataswitch.input.TxtHdfsInput;
import com.huya.lighthouse.dataswitch.output.JdbcOutput;
import com.huya.lighthouse.dataswitch.util.InputOutputUtil;
import com.huya.lighthouse.model.bo.def.DefTaskToJdbc;
import com.huya.lighthouse.plugin.AbstractPlugin;

public class ToJdbcMain extends AbstractPlugin {

	private static Logger logger = LoggerFactory.getLogger(ToJdbcMain.class);
	private static DefTaskToJdbc defTaskToJdbc = null;

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		defTaskToJdbc = (DefTaskToJdbc) readObject();
		exec();
	}

	private static void exec() throws Exception {
		StringBuilder insertSqlBuilder = new StringBuilder();
		insertSqlBuilder.append("INSERT INTO ").append(defTaskToJdbc.getTableName());
		insertSqlBuilder.append("(").append(defTaskToJdbc.getColumns()).append(") VALUES ");
		insertSqlBuilder.append("(:").append(StringUtils.replace(defTaskToJdbc.getColumns(), ",", ",:")).append(")");
		String insertSql = insertSqlBuilder.toString();
		logger.info("\nInsert sql :" + insertSql);

		FileInput fileInput = null;
		JdbcOutput jdbcOutput = null;
		try {
			fileInput = newFileInput(defTaskToJdbc.getDataPaths(), defTaskToJdbc.getColumns(), defTaskToJdbc.getColumnSplit(), defTaskToJdbc.getNullValue());

			jdbcOutput = new JdbcOutput();
			jdbcOutput.setDriverClass(defTaskToJdbc.getJdbcDriver());
			jdbcOutput.setUrl(defTaskToJdbc.getJdbcUrl());
			jdbcOutput.setUsername(defTaskToJdbc.getJdbcUsername());
			jdbcOutput.setPassword(defTaskToJdbc.getJdbcPassword());
			jdbcOutput.setBeforeSql(defTaskToJdbc.getBeforeSql());
			jdbcOutput.setAfterSql(defTaskToJdbc.getAfterSql());
			jdbcOutput.setSql(insertSql);

			InputOutputUtil.copy(fileInput, jdbcOutput, defTaskToJdbc.getBatchUpdateSize());
		} finally {
			IOUtils.closeQuietly(jdbcOutput);
			IOUtils.closeQuietly(fileInput);
		}
	}

	private static FileInput newFileInput(String dataPaths, String columns, String columnSplit, String nullValue) throws Exception {
		FileInput fileInput = null;
		List<String> dirs = new ArrayList<String>();
		if (StringUtils.isNotBlank(dataPaths)) {
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

		if (StringUtils.startsWithIgnoreCase(dirs.get(0), "hdfs://")) {
			TxtHdfsInput txtHdfsInput = new TxtHdfsInput();
			fileInput = txtHdfsInput;
			txtHdfsInput.setDirs(dirs);
			txtHdfsInput.setColumns(columns);
			txtHdfsInput.setColumnSplit(columnSplit);
			txtHdfsInput.setNullValue(nullValue);
		} else {
			TxtFileInput txtFileInput = new TxtFileInput();
			fileInput = txtFileInput;
			txtFileInput.setDirs(dirs);
			txtFileInput.setColumns(columns);
			txtFileInput.setColumnSplit(columnSplit);
			txtFileInput.setNullValue(nullValue);
		}
		return fileInput;
	}
}
