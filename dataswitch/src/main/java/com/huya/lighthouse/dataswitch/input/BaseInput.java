package com.huya.lighthouse.dataswitch.input;

import java.util.ArrayList;
import java.util.List;

import com.huya.lighthouse.dataswitch.BaseObject;

public abstract class BaseInput extends BaseObject implements Input {

	@Override
	public List<Object> read(int size) {
		return read(this, size);
	}

	public static List<Object> read(BaseInput input, int size) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < size; i++) {
			Object obj = input.readObject();
			if (obj == null) {
				break;
			}
			result.add(obj);
		}
		return result;
	}

	public abstract Object readObject();

}
