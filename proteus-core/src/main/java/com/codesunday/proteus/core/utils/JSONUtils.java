/*
 * Copyright (C) 2016  Arun Kumar Selvaraj

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.codesunday.proteus.core.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Utility methods to work on Json objects and arrays.
 * 
 * @author Arun Kumar Selvaraj
 *
 */
public class JSONUtils implements Cloneable, Serializable {

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * Get list of values for a specified search key field.
	 * 
	 * @param input
	 * @param searchKey
	 * @return
	 */
	public static List<JsonNode> getValue(ObjectNode input, String searchKey) {

		List<JsonNode> returnList = new ArrayList<JsonNode>();

		JsonNode currentJsonElement = input;

		for (String key : searchKey.split("\\.")) {
			currentJsonElement = pullDataFromJson(currentJsonElement, key);
		}

		List<JsonNode> intermediateList = jsonToObjectArray(currentJsonElement);

		for (JsonNode obj : intermediateList) {
			returnList.add(obj);
		}

		return returnList;
	}

	/**
	 * Get list of values for a specified search key field.
	 * 
	 * @param input
	 * @param searchKey
	 * @return
	 */
	public static List<String> getValueAsTextual(ObjectNode input, String searchKey) {

		List<String> returnList = new ArrayList<String>();

		JsonNode currentJsonElement = input;

		for (String key : searchKey.split("\\.")) {
			currentJsonElement = pullDataFromJson(currentJsonElement, key);
		}

		List<JsonNode> intermediateList = jsonToObjectArray(currentJsonElement);

		for (JsonNode obj : intermediateList) {
			returnList.add(obj.getTextValue());
		}

		return returnList;
	}

	/**
	 * Recursive algorithm to get the value from a deeply nested Json structure.
	 * 
	 * @param inputObject
	 * @param key
	 * @return
	 */
	private static JsonNode pullDataFromJson(JsonNode inputObject, String key) {

		JsonNode returnObject = null;

		if (inputObject.isObject()) {
			if (inputObject.has(key)) {
				returnObject = inputObject.get(key);
			}

		} else if (inputObject.isArray()) {

			ArrayNode returnArray = mapper.createArrayNode();

			for (JsonNode node : inputObject) {

				JsonNode node2 = pullDataFromJson(node, key);

				if (node2.isObject()) {
					returnArray.add(node2);
				} else if (node2.isArray()) {
					for (JsonNode arrNode : node2) {
						returnArray.add(arrNode);
					}
				} else {
					returnArray.add(node2);
				}
			}

			returnObject = returnArray;

		}

		return returnObject;
	}

	/**
	 * Convert Json object or Json array into a list of objects
	 * 
	 * @param input
	 * @return
	 */
	private static List<JsonNode> jsonToObjectArray(JsonNode input) {
		List<JsonNode> returnList = new ArrayList<JsonNode>();
		if (input != null) {
			if (input.isArray()) {

				for (JsonNode node : input) {
					List<JsonNode> intermediate;
					intermediate = jsonToObjectArray(node);
					returnList.addAll(intermediate);

				}
			} else if (input instanceof ObjectNode) {

				returnList.add(input);

			} else {
				returnList.add(input);
			}
		}
		return returnList;
	}

	/**
	 * Merge two Json objects.
	 * 
	 * @param copyTo
	 * @param copyFrom
	 */
	public static void putAll(ObjectNode copyTo, ObjectNode copyFrom) {
		Iterator<String> keys = copyFrom.getFieldNames();
		while (keys.hasNext()) {
			String key = keys.next();
			put(copyTo, key, copyFrom.get(key));
		}
	}

	/**
	 * Add a key value pair to a Json object. This method is to add any
	 * additional logic that may be required in the future to be applied before
	 * ading an element into Json.
	 * 
	 * @param input
	 * @param key
	 * @param value
	 */
	public static void put(ObjectNode input, String key, JsonNode value) {
		if (value instanceof ObjectNode) {
			input.put(key, value);
		}
	}

}
