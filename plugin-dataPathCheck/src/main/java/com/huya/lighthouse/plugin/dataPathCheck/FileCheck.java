package com.huya.lighthouse.plugin.dataPathCheck;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.DefTaskDataPathCheck;
import com.huya.lighthouse.util.FileSizeUtils;

public class FileCheck {

	protected static Logger logger = LoggerFactory.getLogger(FileCheck.class);

	public static void check(DefTaskDataPathCheck defTaskDataPathCheck) throws Exception {
		File dataPath = FileManager.createFile(defTaskDataPathCheck.getDataPath());
		if (!dataPath.exists()) {
			throw new Exception("not found dataPath: " + defTaskDataPathCheck.getDataPath());
		}
		Iterator<File> files = FileUtils.iterateFiles(dataPath, null, true);

		boolean foundAnyFile = false;
		long fileCount = 0;
		long sumFileSize = 0;
		long sumFileRow = 0;
		while (files.hasNext()) {
			File file = files.next();
			if (file.isFile() && !file.isHidden()) {
				try {
					if (defTaskDataPathCheck.getSumFileRow() > 0) {
						sumFileRow += getFileLineNum(file);
					}
					if (file.length() > 0) {
						fileCount++;
					}
					foundAnyFile = true;
					sumFileSize += file.length();
				} catch (Exception e) {
					throw new IllegalStateException("error on file:" + file, e);
				}
			}
		}

		if (defTaskDataPathCheck.getLongSumFileSize() > 0 && defTaskDataPathCheck.getLongSumFileSize() > sumFileSize) {
			throw new Exception("sumFileSize error, expected sumFileSize: " + defTaskDataPathCheck.getSumFileSize() + " actual sumFileSize: " + FileSizeUtils.getHumanReadableFileSize(sumFileSize));
		}

		if (defTaskDataPathCheck.getFileCount() > 0 && defTaskDataPathCheck.getFileCount() > fileCount) {
			throw new Exception("fileCount error, expected fileCount:" + defTaskDataPathCheck.getFileCount() + " actual fileCount:" + fileCount);
		}

		if (defTaskDataPathCheck.getSumFileRow() > 0 && defTaskDataPathCheck.getSumFileRow() > sumFileRow) {
			throw new Exception("sumFileRow error, expected sumFileRow: " + defTaskDataPathCheck.getSumFileRow() + " actual sumFileRow: " + sumFileRow);
		}

		logger.info("sumFileSize: " + sumFileSize + " fileCount: " + fileCount + " sumFileRow: " + sumFileRow + " foundAnyFile: " + foundAnyFile);
		if (!foundAnyFile) {
			throw new Exception("not found any file by:" + defTaskDataPathCheck.getDataPath());
		}
	}

	public static int getFileLineNum(File file) {
		long fileLength = file.length();
		if (fileLength == 0) {
			return 0;
		}
		LineNumberReader rf = null;
		int lineNum = 0;
		try {
			rf = new LineNumberReader(new FileReader(file));
			if (rf != null) {
				rf.skip(fileLength);
				lineNum = rf.getLineNumber();
				lineNum++;
				rf.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
		}
		return lineNum;
	}

}
