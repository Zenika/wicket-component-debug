package com.zenika.wicket.component.debug.utils;

import java.util.Map;

public final class JsonUtils {

	private JsonUtils() {

	}

	public static String mapToJsonString(Map<String, String> map) {
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append("{ ");
		for (Map.Entry<String, String> e : map.entrySet()) {
			buffer.append("'").append(e.getKey()).append("'");
			buffer.append(" : ");
			buffer.append("'").append(e.getValue()).append("'");
			if (++i < map.size())
				buffer.append(" , ");
		}
		buffer.append(" }");
		return buffer.toString();
	}

}
