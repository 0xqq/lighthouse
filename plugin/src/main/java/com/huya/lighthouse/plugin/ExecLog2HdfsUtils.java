package com.huya.lighthouse.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.dataswitch.util.HdfsFile;
import com.huya.lighthouse.util.ShellExecUtils;

public class ExecLog2HdfsUtils {

	protected static Logger logger = LoggerFactory.getLogger(ExecLog2HdfsUtils.class);
	
	public static void main(String[] args) throws Exception {
		String program = args[0];
		String hdfsPath = args[1];
		String hdfsSite = "hdfs-site.xml";
		logger.info("hdfsSite = " + hdfsSite);
		InputStream hdfsInputStream = ExecLog2HdfsUtils.class.getClassLoader().getResourceAsStream(hdfsSite);
		FileSystem fs = HdfsFile.getFileSystem(hdfsPath, hdfsInputStream);
		HdfsFile hdfsFile = new HdfsFile(fs, hdfsPath);
		hdfsFile.createNewFile();
		OutputStream outputStream = hdfsFile.append(1024);
		final FSDataOutputStream fsOut = (FSDataOutputStream) outputStream;

		ScheduledExecutorService cron = Executors.newScheduledThreadPool(1);
		cron.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					fsOut.hflush();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}, 5, 5, TimeUnit.SECONDS);

		boolean isSuccess = false;
		try {
			ShellExecUtils.exec(program, outputStream, outputStream, 0);
			isSuccess = true;
		} catch (Exception e) {
			fsOut.writeBytes(ExceptionUtils.getFullStackTrace(e));
		} finally {
			cron.shutdown();
			IOUtils.closeQuietly(outputStream);
		}
		
		if (isSuccess) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
