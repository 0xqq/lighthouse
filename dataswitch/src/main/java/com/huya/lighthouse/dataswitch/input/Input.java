package com.huya.lighthouse.dataswitch.input;

import java.io.Closeable;
import java.util.List;

public interface Input extends Closeable {

	public List<Object> read(int size);

	// public Object readObject();

}
