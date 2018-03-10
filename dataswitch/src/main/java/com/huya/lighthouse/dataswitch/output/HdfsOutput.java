package com.huya.lighthouse.dataswitch.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.apache.hadoop.fs.FileSystem;

import com.huya.lighthouse.dataswitch.util.HdfsFile;

public class HdfsOutput extends FileOutput {

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
	protected OutputStream openOutputStream(File file) throws FileNotFoundException {
		HdfsFile f = (HdfsFile) file;
		return f.create(); // create new file and override
	}

}
