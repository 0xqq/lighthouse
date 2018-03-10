package com.huya.lighthouse.plugin.toRedis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;

import com.huya.lighthouse.dataswitch.input.FileInput;
import com.huya.lighthouse.dataswitch.input.TxtFileInput;
import com.huya.lighthouse.dataswitch.input.TxtHdfsInput;
import com.huya.lighthouse.dataswitch.output.RedisOutput;
import com.huya.lighthouse.dataswitch.util.InputOutputUtil;
import com.huya.lighthouse.model.bo.def.DefTaskToRedis;
import com.huya.lighthouse.plugin.AbstractPlugin;

public class ToRedisMain extends AbstractPlugin {

	protected static Logger logger = LoggerFactory.getLogger(ToRedisMain.class);
	private static DefTaskToRedis defTaskToRedis = null;

	public static void main(String[] args) throws Exception {
		instanceDir = args[0];
		defTaskToRedis = (DefTaskToRedis) readObject();
		exec();
	}

	private static void exec() throws Exception {
		FileInput fileInput = null;
		RedisOutput redisOutput = null;
		String password = StringUtils.isBlank(defTaskToRedis.getPassword()) ? null : defTaskToRedis.getPassword();
		JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(), defTaskToRedis.getHost(), defTaskToRedis.getPort(), defTaskToRedis.getTimeout(), password, defTaskToRedis.getDatabase());
		try {
			fileInput = newFileInput(defTaskToRedis.getDataPaths(), defTaskToRedis.getColumns(), defTaskToRedis.getColumnSplit(), defTaskToRedis.getNullValue());

			redisOutput = new RedisOutput();
			redisOutput.setJedisPool(jedisPool);
			redisOutput.setScript(defTaskToRedis.getScript());

			InputOutputUtil.copy(fileInput, redisOutput, defTaskToRedis.getBatchUpdateSize());
		} finally {
			IOUtils.closeQuietly(redisOutput);
			IOUtils.closeQuietly(fileInput);
			jedisPool.close();
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
