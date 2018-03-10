package com.huya.lighthouse.dataswitch.serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.serializer.Deserializer;
import org.springframework.util.Assert;

import com.huya.lighthouse.dataswitch.BaseObject;
import com.huya.lighthouse.dataswitch.Constants;
import com.huya.lighthouse.dataswitch.util.MapUtil;
import com.huya.lighthouse.dataswitch.util.Util;

public class TxtDeserializer extends BaseObject implements Deserializer<Map> {

	/**
	 * 分隔符
	 **/
	private String columnSplit = Constants.COLUMN_SPLIT;

	/**
	 * hive 的null 值特殊转义字符
	 */
	private String nullValue = Constants.NULL_VALUE;

	/**
	 * 数据列
	 */
	private String columns;

	private String charset = null;

	private transient String[] columnNames;
	private transient boolean isInit = false;

	private Map<InputStream, BufferedReader> cache = new HashMap<InputStream, BufferedReader>();

	public TxtDeserializer() {
	}

	public TxtDeserializer(TxtDeserializer in) {
		this.setColumns(in.getColumns());
		this.setNullValue(in.getNullValue());
		this.setColumnSplit(in.getColumnSplit());
		this.setCharset(in.getCharset());
	}

	public String getColumnSplit() {
		return columnSplit;
	}

	public void setColumnSplit(String columnSplit) {
		this.columnSplit = columnSplit;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	private Map toMap(String line, String[] columnNames) {
		String[] columnValues = splitLine(line, columnSplit);
		return MapUtil.toMap(columnValues, columnNames);
	}

	private String[] splitLine(String line, String columnSplit) {
		String[] array = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(line, columnSplit);
		for (int i = 0; i < array.length; i++) {
			if (nullValue.equals(array[i])) {
				array[i] = null;
			}
		}
		return array;
	}

	public Map read(BufferedReader reader) {
		try {
			String line = reader.readLine();
			if (line == null) {
				return null;
			}
			if (StringUtils.isBlank(line)) {
				return read(reader);
			}
			if (line.startsWith("#")) {
				return read(reader);
			}
			return toMap(line, columnNames);
		} catch (Exception e) {
			throw new RuntimeException("read() error,id:" + getId(), e);
		}
	}

	private void init() {
		Assert.hasText(columns, "columns must be not empty");
		columnNames = Util.splitColumns(columns);
	}

	@Override
	public Map deserialize(InputStream inputStream) throws IOException {
		if (!isInit) {
			isInit = true;
			init();
		}
		BufferedReader in = cache.get(inputStream);
		if (in == null) {
			synchronized (cache) {
				in = new BufferedReader(new InputStreamReader(inputStream));
				cache.put(inputStream, in);
			}
		}
		return read(in);
	}

}
