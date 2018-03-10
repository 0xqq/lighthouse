package com.huya.lighthouse.dataswitch.serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.huya.lighthouse.dataswitch.BaseObject;
import com.huya.lighthouse.dataswitch.util.IOUtil;

public class ByteSerializer extends BaseObject implements Serializer {

	@Override
	public void serialize(Object object, OutputStream outputStream) throws IOException {
		byte[] buf = object2Bytes(object);
		IOUtil.writeWithLength(new DataOutputStream(outputStream), buf);
	}

	protected byte[] object2Bytes(Object object) throws IOException {
		return IOUtil.javaObject2Bytes(object);
	}

	@Override
	public void flush() throws IOException {
	}

}
