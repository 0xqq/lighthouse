package com.huya.lighthouse.dataswitch.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

public class CompressUtil {

	static Map<String, String> compressExtionsionMap = new HashMap<String, String>();
	static {
		compressExtionsionMap.put("gzip", "gz");
		compressExtionsionMap.put("snappy", "snappy");
	}

	public static String getExtionsionByCompressType(String compressType) {
		return compressExtionsionMap.get(compressType);
	}

	public static OutputStream newCompressOutput(String compressType, OutputStream output) throws IOException {
		if ("gzip".equals(compressType)) {
			return new GZIPOutputStream(output);
		} else if ("bzip2".equals(compressType)) {
			throw new RuntimeException("not yet impl");
		} else if ("snappy".equals(compressType)) {
			return new SnappyOutputStream(output);
		} else {
			return output;
		}
	}

	public static InputStream newDecompressInputByExt(InputStream input, String filename) throws IOException {
		String ext = FilenameUtils.getExtension(filename);
		if ("gz".equalsIgnoreCase(ext)) {
			return new GZIPInputStream(input);
		} else if ("snappy".equalsIgnoreCase(ext)) {
			return new SnappyInputStream(input);
		} else if ("bz2".equalsIgnoreCase(ext)) {
			throw new RuntimeException("not yet impl,bzip2");
		} else {
			return input;
		}
	}

}
