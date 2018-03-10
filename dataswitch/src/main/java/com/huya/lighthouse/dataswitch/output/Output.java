package com.huya.lighthouse.dataswitch.output;

import java.io.Closeable;
import java.util.List;

public interface Output extends Closeable {

	public void write(List<Object> rows);

}
