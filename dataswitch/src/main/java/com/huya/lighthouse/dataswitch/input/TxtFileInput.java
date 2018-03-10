package com.huya.lighthouse.dataswitch.input;

import com.huya.lighthouse.dataswitch.serializer.TxtDeserializer;

public class TxtFileInput extends FileInput {

	private TxtDeserializer txtDeserializer = new TxtDeserializer();

	public TxtFileInput() {
		setDeserializer(txtDeserializer);
	}

	public String getColumnSplit() {
		return txtDeserializer.getColumnSplit();
	}

	public void setColumnSplit(String columnSplit) {
		txtDeserializer.setColumnSplit(columnSplit);
	}

	public String getNullValue() {
		return txtDeserializer.getNullValue();
	}

	public void setNullValue(String nullValue) {
		txtDeserializer.setNullValue(nullValue);
	}

	public String getColumns() {
		return txtDeserializer.getColumns();
	}

	public void setColumns(String columns) {
		txtDeserializer.setColumns(columns);
	}

	public String getCharset() {
		return txtDeserializer.getCharset();
	}

	public void setCharset(String charset) {
		txtDeserializer.setCharset(charset);
	}

}
