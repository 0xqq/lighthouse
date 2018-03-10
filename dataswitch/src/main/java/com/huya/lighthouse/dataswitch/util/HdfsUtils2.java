package com.huya.lighthouse.dataswitch.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsUtils2 {

	protected static Logger logger = LoggerFactory.getLogger(HdfsUtils2.class);

	private static FileSystem fs = null;

	public static void getLog(OutputStream outputStream, String hdfsPath) throws Exception {
		if (fs == null) {
			initFileSystem(hdfsPath);
		}
		HdfsFile hdfsFile = new HdfsFile(fs, hdfsPath);
		if (!hdfsFile.exists()) {
			return;
		}
		InputStream inputStream = hdfsFile.open();
		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public static void appendHdfs(String msg, String hdfsPath) {
		OutputStream hdfsOS = null;
		try {
			if (fs == null) {
				initFileSystem(hdfsPath);
			}
			HdfsFile hdfsFile = new HdfsFile(fs, hdfsPath);
			hdfsFile.createNewFile();
			hdfsOS = hdfsFile.append();
			IOUtils.write(msg, hdfsOS);
		} catch (Exception e) {
			logger.error("appendHdfs error", e);
		} finally {
			IOUtils.closeQuietly(hdfsOS);
		}
	}

	private static synchronized void initFileSystem(String hdfsPath) throws Exception {
		if (fs != null) {
			return;
		}
		String hdfsSite = "hdfs-site.xml";
		logger.info("hdfsSite = " + hdfsSite);
		InputStream hdfsInputStream = HdfsUtils2.class.getClassLoader().getResourceAsStream(hdfsSite);
		fs = HdfsFile.getFileSystem(hdfsPath, hdfsInputStream);
	}
	
	public static void copyHdfs2local(List<String> hdfsDirs, String localDir) throws Exception {
		if (hdfsDirs == null) {
			return;
		}
		FileSystem fs = null;
		for (int i = 0; i < hdfsDirs.size(); i++) {
			String hdfsDir = hdfsDirs.get(i);
			if (i == 0) {
				fs = HdfsFile.getFileSystem(hdfsDir);
			}
			List<File> files = FileUtils2.listAllFiles(newHdfsFile(fs, hdfsDir));
			if (files == null) {
				continue;
			}
			for (int j = 0; j < files.size(); j++) {
				File file = files.get(i);
				String fileName = file.getName() + "_" + i + "_" + j;
				fs.copyToLocalFile(new Path(file.getPath()), new Path(localDir + "/" + fileName));
			}
		}
	}

	public static File newHdfsFile(FileSystem fs, String dir) {
		return new HdfsFile(fs, dir);
	}
}
