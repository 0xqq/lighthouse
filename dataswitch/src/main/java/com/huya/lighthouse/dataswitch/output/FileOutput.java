package com.huya.lighthouse.dataswitch.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.huya.lighthouse.dataswitch.serializer.Serializer;
import com.huya.lighthouse.dataswitch.util.CompressUtil;

public class FileOutput extends BaseOutput implements Output {

	private static Logger log = LoggerFactory.getLogger(FileOutput.class);

	private String dir; // 文件存储目录
	private int maxFileSize; // 最大文件大小
	private String charset; // 输入编码
	private String compressType; // 压缩类型: gzip, snappy,bzip2

	private transient Serializer serializer;
	private transient boolean isInit = false;

	private OutputStream outputStream;

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getCompressType() {
		return compressType;
	}

	public void setCompressType(String compressType) {
		this.compressType = compressType;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	private void init() throws IOException {
		Assert.notNull(serializer, "serializer must be not null");
		log.info("clean dir:" + newFile(dir));
		FileUtils.deleteDirectory(newFile(dir));
		File file = newFile(newFilenameByCompressType());
		log.info("mkdirs dir:" + newFile(dir));
		file.getParentFile().mkdirs();

		log.info("fileoutput:" + file.getPath());
		outputStream = new BufferedOutputStream(CompressUtil.newCompressOutput(compressType, openOutputStream(file)));
	}

	private String newFilenameByCompressType() {
		String ext = CompressUtil.getExtionsionByCompressType(compressType);
		if (StringUtils.isNotBlank(compressType) && StringUtils.isBlank(ext)) {
			throw new RuntimeException("not found ext for compressType:" + compressType);
		}
		return dir + "/00000" + (ext == null ? "" : "." + ext);
	}

	protected OutputStream openOutputStream(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	protected File newFile(String path) {
		return new File(path);
	}

	@Override
	public void close() {
		try {
			serializer.flush();
		} catch (IOException e) {
			throw new RuntimeException("flush error", e);
		}
		IOUtils.closeQuietly(outputStream);
	}

	@Override
	public void writeObject(Object object) {
		try {
			if (!isInit) {
				isInit = true;
				init();
			}

			serializer.serialize(object, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("write error,id:" + getId(), e);
		}
	}
}
