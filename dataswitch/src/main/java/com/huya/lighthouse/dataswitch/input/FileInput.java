package com.huya.lighthouse.dataswitch.input;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;
import org.springframework.util.Assert;

import com.huya.lighthouse.dataswitch.util.CompressUtil;
import com.huya.lighthouse.dataswitch.util.FileUtils2;

public class FileInput extends BaseInput implements Input {

	private static Logger log = LoggerFactory.getLogger(FileInput.class);

	/**
	 * 数据输入的目录
	 */
	private List<String> dirs;

	private Deserializer deserializer = null;

	private List<File> files = null;
	private transient boolean isInit = false;
	private transient InputStream inputStream;

	public List<String> getDirs() {
		return dirs;
	}

	public void setDirs(List<String> dirs) {
		this.dirs = dirs;
	}

	public void setDir(String dir) {
		setDirs(Arrays.asList(dir));
	}

	public Deserializer getDeserializer() {
		return deserializer;
	}

	public void setDeserializer(Deserializer deserializer) {
		this.deserializer = deserializer;
	}

	@Override
	public Object readObject() {
		try {
			if (!isInit) {
				Assert.notNull(deserializer, "deserializer must be not null");
				isInit = true;
				this.files = listAllFiles();
				Assert.isTrue(!this.files.isEmpty(), "not found any file by dirs:" + getDirs());
			}

			if (inputStream == null) {
				if (files.isEmpty()) {
					return null;
				}

				File currentFile = files.remove(0);
				log.info("read from file:" + currentFile);
				inputStream = new BufferedInputStream(CompressUtil.newDecompressInputByExt(openFileInputStream(currentFile), currentFile.getName()));
			}

			Object object = deserializer.deserialize(inputStream);
			if (object == null) {
				IOUtils.closeQuietly(inputStream);
				inputStream = null;
				return readObject();
			}
			return object;
		} catch (IOException e) {
			throw new RuntimeException("read error,id:" + getId() + " inputDirs:" + dirs, e);
		}
	}

	protected InputStream openFileInputStream(File currentFile) throws FileNotFoundException {
		return new FileInputStream(currentFile);
	}

	private List<File> listAllFiles() {
		return listAllFiles(dirs);
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(inputStream);
	}

	public List<File> listAllFiles(List<String> dirs) {
		List<File> files = new LinkedList<File>();
		for (String dir : dirs) {
			List tempFiles = FileUtils2.listAllFiles(newFile(dir));
			files.addAll(tempFiles);
		}
		return files;
	}

	protected File newFile(String dir) {
		return new File(dir);
	}

}
