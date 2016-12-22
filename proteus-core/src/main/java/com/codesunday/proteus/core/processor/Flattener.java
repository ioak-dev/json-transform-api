package com.codesunday.proteus.core.processor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class Flattener {

	private static ObjectMapper mapper = new ObjectMapper();

	public static ObjectNode flattenAsJson(ObjectNode input) {

		ObjectNode output = mapper.createObjectNode();

		Iterator<String> fields = input.getFieldNames();

		while (fields.hasNext()) {
			String key = fields.next();

			JsonNode value = input.get(key);

			ObjectNode elements = analyse(key, value);

			concatenateObjectNode(output, elements);

		}

		return output;

	}

	public static ArrayNode flattenAsJson(ArrayNode inputArray) {

		ArrayNode outputArray = mapper.createArrayNode();

		for (JsonNode node : inputArray) {
			if (inputArray.isObject()) {
				ObjectNode output = flattenAsJson((ObjectNode) node);
				outputArray.add(output);
			}

		}

		return outputArray;

	}

	public static ArrayNode flattenAsDelimited(ObjectNode input, String delimiter, String enclosedBy) {

		ArrayNode output = mapper.createArrayNode();
		Set<String> keys = new HashSet<String>();

		if (delimiter == null) {
			delimiter = ",";
		}

		ObjectNode flattenedJson = flattenAsJson(input);

		Iterator<String> fields = flattenedJson.getFieldNames();

		while (fields.hasNext()) {
			keys.add(fields.next());
		}

		if (enclosedBy == null) {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(key);
				headerSb.append(delimiter);
			}

			int end = 0;

			if (headerSb.length() > 1) {
				end = headerSb.length() - 1;
			}

			output.add(headerSb.substring(0, end).toString());

			StringBuilder bodySb = new StringBuilder();

			for (String key : keys) {
				bodySb.append(flattenedJson.get(key));
				bodySb.append(delimiter);
			}

			if (bodySb.length() > 1) {
				end = bodySb.length() - 1;
			}

			output.add(bodySb.substring(0, end).toString());
		} else {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(enclosedBy);
				headerSb.append(key);
				headerSb.append(enclosedBy);
				headerSb.append(delimiter);
			}

			output.add(headerSb.substring(0, headerSb.length() - 1).toString());

			StringBuilder bodySb = new StringBuilder();

			for (String key : keys) {
				bodySb.append(enclosedBy);
				bodySb.append(flattenedJson.get(key));
				bodySb.append(enclosedBy);
				bodySb.append(delimiter);
			}

			output.add(bodySb.substring(0, bodySb.length() - 1).toString());
		}

		return output;

	}

	public static ArrayNode flattenAsDelimited(ArrayNode inputArray, String delimiter, String enclosedBy) {

		ArrayNode output = mapper.createArrayNode();
		Set<String> keys = new HashSet<String>();

		if (delimiter == null) {
			delimiter = ",";
		}

		ArrayNode flattenedArrayNode = mapper.createArrayNode();

		for (JsonNode node : inputArray) {

			ObjectNode flattenedJson = flattenAsJson((ObjectNode) node);

			flattenedArrayNode.add(flattenedJson);

			Iterator<String> fields = flattenedJson.getFieldNames();

			while (fields.hasNext()) {
				keys.add(fields.next());
			}

		}

		if (enclosedBy == null) {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(key);
				headerSb.append(delimiter);
			}

			int end = 0;

			if (headerSb.length() > 1) {
				end = headerSb.length() - 1;
			}

			output.add(headerSb.substring(0, end).toString());

			for (JsonNode node : flattenedArrayNode) {

				StringBuilder bodySb = new StringBuilder();

				for (String key : keys) {
					if (node.has(key)) {
						bodySb.append(node.get(key));
					} else {
						bodySb.append("");
					}
					bodySb.append(delimiter);
				}

				end = 0;

				if (bodySb.length() > 1) {
					end = bodySb.length() - 1;
				}

				output.add(bodySb.substring(0, end).toString());
			}

		} else {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(enclosedBy);
				headerSb.append(key);
				headerSb.append(enclosedBy);
				headerSb.append(delimiter);
			}

			int end = 0;

			if (headerSb.length() > 1) {
				end = headerSb.length() - 1;
			}

			output.add(headerSb.substring(0, end).toString());

			for (JsonNode node : flattenedArrayNode) {

				StringBuilder bodySb = new StringBuilder();

				for (String key : keys) {
					bodySb.append(enclosedBy);
					if (node.has(key)) {
						bodySb.append(node.get(key));
					} else {
						bodySb.append("");
					}
					bodySb.append(enclosedBy);
					bodySb.append(delimiter);
				}

				end = 0;

				if (bodySb.length() > 1) {
					end = bodySb.length() - 1;
				}

				output.add(bodySb.substring(0, end).toString());
			}

		}

		return output;

	}

	private static void concatenateObjectNode(ObjectNode destination, ObjectNode source) {

		Iterator<String> iterator = source.getFieldNames();

		while (iterator.hasNext()) {
			String key = iterator.next();
			destination.put(key, source.get(key));
		}
	}

	private static ObjectNode analyse(String baseKey, JsonNode inputObj) {

		ObjectNode output = mapper.createObjectNode();

		if (inputObj instanceof ObjectNode) {

			ObjectNode objectNode = (ObjectNode) inputObj;

			Iterator<String> iterator = objectNode.getFieldNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				JsonNode value = objectNode.get(key);

				concatenateObjectNode(output, analyse(baseKey + "." + key, value));
			}

		} else if (inputObj instanceof ArrayNode)

		{

			int i = 0;

			for (JsonNode node : inputObj) {
				concatenateObjectNode(output, analyse(baseKey + "[" + i + "]", node));
				i = i + 1;
			}

		} else {
			output.put(baseKey, inputObj);
		}

		return output;

	}

}
