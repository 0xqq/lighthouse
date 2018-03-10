package com.huya.lighthouse.dataswitch.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileUtils2 {

	public static List<File> listAllFiles(File dir) {
		Collection<File> files = null;
		if (dir.isDirectory()) {
			files = FileUtils.listFiles(dir, null, true);
		} else {
			files = Arrays.asList(dir);
		}

		List<File> result = new ArrayList<File>();
		for (File f : files) {
			if (f.isDirectory()) {
				continue;
			}
			if (f.isHidden()) {
				continue;
			}
			if (f.getName().startsWith("_")) {
				continue;
			}
			if (f.getName().startsWith(".")) {
				continue;
			}
			if (f.getPath().contains(".svn")) {
				continue;
			}
			if (f.length() <= 0) {
				continue;
			}
			if (f.isFile()) {
				result.add(f);
			}
		}
		return result;
	}
	
}
