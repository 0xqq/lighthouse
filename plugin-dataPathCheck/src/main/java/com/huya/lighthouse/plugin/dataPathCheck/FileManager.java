package com.huya.lighthouse.plugin.dataPathCheck;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;

import com.huya.lighthouse.dataswitch.util.HdfsFile;

public class FileManager {

	public static File createFile(String path) throws IOException {
		if (path.startsWith("hdfs://")) {
			try {
				return new HdfsFile(path);
			} catch (Exception e) {
				throw new IOException(e);
			}
		} else {
			return new File(path);
		}
	}

	public static void closeFileSystem(FileSystem fs) {
		if (fs != null) {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
