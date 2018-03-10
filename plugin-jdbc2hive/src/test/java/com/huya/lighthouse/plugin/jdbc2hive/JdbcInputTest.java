package com.huya.lighthouse.plugin.jdbc2hive;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;

import com.huya.lighthouse.dataswitch.input.JdbcInput;
import com.huya.lighthouse.dataswitch.output.FileOutput;
import com.huya.lighthouse.dataswitch.serializer.TxtSerializerExt;
import com.huya.lighthouse.dataswitch.util.InputOutputUtil;
import com.huya.lighthouse.model.bo.def.FromJdbcSep;

public class JdbcInputTest {

	@Test
	public void test1() throws Exception {
		JdbcInput jdbcInput = new JdbcInput();
		jdbcInput.setDriverClass("com.mysql.jdbc.Driver");
		jdbcInput.setUrl("jdbc:mysql://14.215.104.210:3306/ds?useUnicode=true&amp;characterEncoding=utf-8");
		jdbcInput.setUsername("alldata");
		jdbcInput.setPassword("123456789");
//		jdbcInput.setSql("select UID,PID,Score,Quota,QuotaUsed,QuotaTS,null as UpdateTS,IF(OpenTS='0000-00-00 00:00:00', '0000-00-00 00:00:00', date_format(OpenTS, '%Y-%c-%d %h:%i:%s')) OpenTS from ReducedBadgeInfo where 1=0");
		jdbcInput.setSql("select col2 from test");
//		jdbcInput.setSql("select source_client from upload_list");
//		List<Object> result = jdbcInput.read(10);
//		System.out.println(result);

		TxtSerializerExt serializer = new TxtSerializerExt();
		FileOutput fileOutput = new FileOutput();
		fileOutput.setDir("F:/data/ds");
		fileOutput.setSerializer(serializer);
		InputOutputUtil.copy(jdbcInput, fileOutput);

		IOUtils.closeQuietly(fileOutput);
		IOUtils.closeQuietly(jdbcInput);
	}

	@Test
	public void test2() throws Exception {
		Date date = DateUtils.parseDate("0000-00-00 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss" });
		System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
	}

	@Test
	public void test3() throws Exception {
		File f = new File("F:/data/lighthouse/tmp.txt");
		f.createNewFile();
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}

	@Test
	public void test4() throws Exception {
		File f = new File("F:/data/lighthouse/a");
		f.mkdirs();
		FileUtils.moveFile(new File("F:/data/lighthouse/a/abc.txt"), new File("F:/data/lighthouse/aa"));
	}

	@Test
	public void test5() throws Exception {
		List<FromJdbcSep> fromJdbcSepList = new ArrayList<FromJdbcSep>();
		FromJdbcSep f = new FromJdbcSep();
		f.setJdbcDriver("com.mysql.jdbc.Driver");
		f.setJdbcUrl("jdbc:mysql://14.215.104.210:3306/ds?useUnicode=true&amp;characterEncoding=utf-8");
		f.setJdbcUsername("alldata");
		f.setJdbcPassword("123456789");
		f.setSql("select UID,PID,Score,Quota,QuotaUsed,QuotaTS,null as UpdateTS,IF(OpenTS='0000-00-00 00:00:00', '0000-00-00 00:00:00', date_format(OpenTS, '%Y-%c-%d %h:%i:%s')) OpenTS from ReducedBadgeInfo where 1=0");
		fromJdbcSepList.add(f);
		if (fromJdbcSepList == null || fromJdbcSepList.size() == 0) {
			throw new Exception("fromJdbcSepList cannot be null");
		}
		ExecutorService es = Executors.newFixedThreadPool(fromJdbcSepList.size() > 5 ? 5 : fromJdbcSepList.size());
		List<Future<String>> futureList = new ArrayList<Future<String>>();
		for (final FromJdbcSep fromJdbcSep : fromJdbcSepList) {
			Future<String> future = es.submit(new Callable<String>() {
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

				private String getDataDir() {
					return "F:/data/lighthouse/test";
				}
			});
			futureList.add(future);
		}

		for (Future<String> future : futureList) {
			future.get();
		}
	}

}
