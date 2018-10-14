package com.lyy.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lyy
 */
public class JsonSerialization implements Serialization {

	private ObjectMapper objectMapper;
	
	public JsonSerialization() {
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public <T> byte[] serialize(T obj) {
		try {
			return this.objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> T deSerialize(byte[] data, Class<T> clz) {
		try {
			return this.objectMapper.readValue(data, clz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
