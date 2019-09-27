package com.clink;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainTest {
	public static void main(String[] args) throws JsonProcessingException {

		Map<String, String> data = new HashMap<>();
		data.put("errorMessage", "true");
		ObjectMapper map = new ObjectMapper();
		// map.readValue(data, String.class);
		String dataStr = map.writeValueAsString(data);
		System.out.println(dataStr);

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 199; i++) {
			buf.append("?").append(",");
		}
		System.out.println(buf);

		MainTest.extractData();
	}

	static void extractData() {}
}
