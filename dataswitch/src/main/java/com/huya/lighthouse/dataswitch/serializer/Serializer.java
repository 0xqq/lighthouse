package com.huya.lighthouse.dataswitch.serializer;

import java.io.Flushable;

public interface Serializer<T> extends org.springframework.core.serializer.Serializer<T>, Flushable {
}
