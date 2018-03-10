package com.huya.lighthouse.dataswitch.serializer;

import java.io.IOException;
import java.io.Writer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class TxtSerializerExt extends TxtSerializer {
	
	public TxtSerializerExt() {
		setColumns("NULL");
	}

	@Override
	public void write(Writer out,Object row) {
		try {
			List<String> values = new ArrayList<String>();
			if(row instanceof LinkedHashMap) {
				@SuppressWarnings("unchecked")
				Iterator<Entry<String, Object>> iter = ((LinkedHashMap<String, Object>)row).entrySet().iterator();
				while(iter.hasNext()) {
					values.add( format( iter.next().getValue() ) );
				}
			}
			out.write(StringUtils.join(values,getColumnSplit()));
			out.write(getLineSplit());
		}catch(IOException e) {
			throw new RuntimeException("write() error,id:"+getId(),e);
		}
	}
	
	
	private String format(Object value) {
		if(value == null) {
			return getNullValue();
		}
		if(value instanceof Date) {
			return DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:ss");
		}
		return value.toString();
	}
}
