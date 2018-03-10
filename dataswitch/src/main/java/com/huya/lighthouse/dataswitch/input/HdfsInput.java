package com.huya.lighthouse.dataswitch.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.hadoop.fs.FileSystem;

import com.huya.lighthouse.dataswitch.util.HdfsFile;

public class HdfsInput extends FileInput {

	private FileSystem fs = null;

	@Override
	protected File newFile(String dir) {
		if (fs == null) {
			try {
				fs = HdfsFile.getFileSystem(dir);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return new HdfsFile(fs, dir);
	}

	@Override
	protected InputStream openFileInputStream(File currentFile) throws FileNotFoundException {
		HdfsFile f = (HdfsFile) currentFile;
		return f.open();
	}
}
