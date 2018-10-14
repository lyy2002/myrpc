package com.lyy.api;
/**
 * @author lyy
 */
public interface Serialization {

	<T> byte[] serialize(T obj);
	<T> T deSerialize(byte[] data, Class<T> clz);
}
