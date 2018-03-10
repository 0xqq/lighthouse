package com.huya.lighthouse.dataswitch.serializer;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.huya.lighthouse.dataswitch.BaseObject;
import com.huya.lighthouse.dataswitch.Constants;
import com.huya.lighthouse.dataswitch.util.Util;

public class TxtSerializer extends BaseObject implements Serializer<Object>, Flushable {

	private static Logger log = LoggerFactory.getLogger(TxtSerializer.class);
	private String nullValue = Constants.NULL_VALUE;

	private String columns; // 输出列
	private String columnSplit = Constants.COLUMN_SPLIT; // 列分隔符
	private String lineSplit = "\n"; // 行分隔符

	private String charset;
	private String[] columnNames;
	private boolean isInit = false;

	public TxtSerializer() {
	}

	public TxtSerializer(TxtSerializer so) {
		setLineSplit(so.getLineSplit());
		setNullValue(so.getNullValue());
		setColumns(so.getColumns());
		setColumnSplit(so.getColumnSplit());
		setCharset(so.getCharset());
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

	public String getColumnSplit() {
		return columnSplit;
	}

	public void setColumnSplit(String columnSplit) {
		this.columnSplit = columnSplit;
	}

	public String getLineSplit() {
		return lineSplit;
	}

	public void setLineSplit(String lineSplit) {
		this.lineSplit = lineSplit;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void write(Writer out, Object row) {
		try {
			List<String> values = new ArrayList<String>();
			for (String name : columnNames) {
				Object value = null;
				try {
					value = PropertyUtils.getSimpleProperty(row, name);
				} catch (Exception e) {
					throw new RuntimeException("PropertyUtils.getSimpleProperty error, name:" + name, e);
				}
				values.add(format(value));
			}
			out.write(StringUtils.join(values, columnSplit));
			out.write(lineSplit);
		} catch (IOException e) {
			throw new RuntimeException("write() error,id:" + getId(), e);
		}
	}

	private String format(Object value) {
		if (value == null) {
			return nullValue;
		}
		if (value instanceof Date) {
			return DateFormatUtils.format((Date) value, "yyyy-MM-dd HH:mm:ss");
		}
		return value.toString();
	}

	private Map<OutputStream, Writer> cache = new HashMap<OutputStream, Writer>();

	private void init() {
		Assert.hasText(columns, "columns must be not empty");
		columnNames = Util.splitColumns(columns);
	}

	@Override
	public void serialize(Object object, OutputStream outputStream) throws IOException {
		if (!isInit) {
			isInit = true;
			init();
		}

		Writer out = cache.get(outputStream);
		if (out == null) {
			synchronized (cache) {
				if (StringUtils.isBlank(charset)) {
					out = new OutputStreamWriter(outputStream);
				} else {
					out = new OutputStreamWriter(outputStream, charset);
				}
				cache.put(outputStream, out);
			}
		}
		write(out, object);
	}

	@Override
	public void flush() throws IOException {
		for (Writer writer : cache.values()) {
			try {
				writer.flush();
			} catch (Exception e) {
				log.error("flosh error", e);
				// ignore
			}
		}
	}

}
