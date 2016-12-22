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

package com.codesunday.proteus.core.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.codesunday.proteus.core.constants.Constants;
import com.codesunday.proteus.core.exception.ProteusException;
import com.codesunday.proteus.core.utils.JSONUtils;

/**
 * Tranformation logic implementation.
 * 
 * @author Arun Kumar Selvaraj
 *
 */
public class Transformer {

	private static ObjectMapper mapper = new ObjectMapper();

	public static ObjectNode transformImpl(ObjectNode input, ObjectNode template) {
		Iterator keys = template.getFieldNames();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			JsonNode value = template.get(key);

			if (value.isObject()) {
				transformImpl(input, (ObjectNode) value);
			}

			else if (value.isTextual()) {

				List<JsonNode> list;
				String findKey = value.getTextValue();

				if (findKey.startsWith(Constants._AS_SET)) {
					Map<String, List<JsonNode>> intermediateMap = new HashMap();
					int referenceLength = 0;
					for (String subkey : findKey.substring(7, findKey.length() - 1).split(",")) {

						String[] parts = subkey.split(Constants.AS_SPACE);

						String alias = null;

						if (parts.length == 2) {
							alias = parts[1];
						} else {
							alias = parts[0].substring(parts[0].lastIndexOf(Constants.DOT) + 1);
						}

						List<JsonNode> intermediateList = JSONUtils.getValue(input, parts[0]);

						referenceLength = intermediateList.size();

						intermediateMap.put(alias, intermediateList);
					}
					list = concatenate(intermediateMap, referenceLength);
				} else {
					list = JSONUtils.getValue(input, findKey);
				}

				if (list != null && list.size() == 0) {
					template.put(key, "");
				} else if (list != null && list.size() == 1) {
					template.put(key, list.get(0));
				} else {
					ArrayNode array = mapper.valueToTree(list);
					template.put(key, array);
				}
			}

		}

		return template;

	}

	/**
	 * Groups multiple lists of objects into a single list of Json objects with
	 * list name as keys of the Json object.
	 * 
	 * @param intermediateMap
	 * @param referenceLength
	 * @return
	 */
	private static List<JsonNode> concatenate(Map<String, List<JsonNode>> intermediateMap, int referenceLength) {

		boolean mergable = true;

		for (String key : intermediateMap.keySet()) {
			if (intermediateMap.get(key).size() != referenceLength) {
				mergable = false;
				break;
			}
		}

		List<JsonNode> returnList = new ArrayList();

		if (mergable) {

			for (int i = 0; i < referenceLength; i++) {
				ObjectNode json = mapper.createObjectNode();
				for (String key : intermediateMap.keySet()) {
					json.put(key, intermediateMap.get(key).get(i));

				}

				returnList.add(json);
			}

		} else {
			throw new ProteusException(
					"Reference keys cannot be combined as a set. Number of values for each key does not match");
		}

		return returnList;
	}
}
