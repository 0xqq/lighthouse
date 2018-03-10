package com.huya.lighthouse.dataswitch.serializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.LinkedList;

import org.springframework.core.serializer.Deserializer;

import com.huya.lighthouse.dataswitch.BaseObject;
import com.huya.lighthouse.dataswitch.util.IOUtil;

public class ByteDeserializer extends BaseObject implements Deserializer {

	private LinkedList<Object> buf = new LinkedList<Object>();

	private Object read(DataInputStream in) {
		try {
			if (!buf.isEmpty()) {
				return buf.removeFirst();
			}
			byte[] bytes = IOUtil.readByLength(in);
			if (bytes != null) {
				Object r = parseBytes(bytes);
				if (r instanceof Collection) {
					buf.addAll((Collection) r);
					return read(in);
				} else {
					return r;
				}
			}
			return null;
		} catch (EOFException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException("read error,id:" + getId(), e);
		}
	}

	protected Object parseBytes(byte[] buf) throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
		return ois.readObject();
	}

	@Override
	public Object deserialize(InputStream inputStream) throws IOException {
		return read(new DataInputStream(inputStream));
	}

}
