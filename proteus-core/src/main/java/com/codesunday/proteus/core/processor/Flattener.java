package com.codesunday.proteus.core.processor;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Flattener {

	public static JSONObject flattenAsJson(JSONObject input) {

		JSONObject output = new JSONObject();

		for (String key : input.keySet()) {
			Object value = input.opt(key);

			JSONObject elements = analyse(key, value);

			concatenateJsonObject(output, elements);

		}

		return output;

	}

	public static JSONArray flattenAsJson(JSONArray inputArray) {

		JSONArray outputArray = new JSONArray();

		for (int i = 0; i < inputArray.length(); i++) {
			JSONObject input = inputArray.optJSONObject(i);
			JSONObject output = flattenAsJson(input);

			outputArray.put(output);
		}

		return outputArray;

	}

	public static JSONArray flattenAsDelimited(JSONObject input, String delimiter, String enclosedBy) {

		JSONArray output = new JSONArray();
		Set<String> keys = new HashSet<String>();

		if (delimiter == null) {
			delimiter = ",";
		}

		JSONObject flattenedJson = flattenAsJson(input);

		keys.addAll(flattenedJson.keySet());

		if (enclosedBy == null) {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(key);
				headerSb.append(delimiter);
			}
			
			int end = 0;
			
			if(headerSb.length()>1){
				end = headerSb.length() - 1;
			}

			output.put(headerSb.substring(0, end).toString());

			StringBuilder bodySb = new StringBuilder();

			for (String key : keys) {
				bodySb.append(flattenedJson.opt(key));
				bodySb.append(delimiter);
			}
			
			if(bodySb.length()>1){
				end = bodySb.length() - 1;
			}

			output.put(bodySb.substring(0, end).toString());
		} else {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(enclosedBy);
				headerSb.append(key);
				headerSb.append(enclosedBy);
				headerSb.append(delimiter);
			}

			output.put(headerSb.substring(0, headerSb.length() - 1).toString());

			StringBuilder bodySb = new StringBuilder();

			for (String key : keys) {
				bodySb.append(enclosedBy);
				bodySb.append(flattenedJson.opt(key));
				bodySb.append(enclosedBy);
				bodySb.append(delimiter);
			}

			output.put(bodySb.substring(0, bodySb.length() - 1).toString());
		}

		return output;

	}

	public static JSONArray flattenAsDelimited(JSONArray inputArray, String delimiter, String enclosedBy) {

		JSONArray output = new JSONArray();
		Set<String> keys = new HashSet<String>();

		if (delimiter == null) {
			delimiter = ",";
		}

		JSONArray flattenedJsonArray = new JSONArray();

		for (int i = 0; i < inputArray.length(); i++) {

			JSONObject flattenedJson = flattenAsJson(inputArray.optJSONObject(i));

			flattenedJsonArray.put(flattenedJson);

			keys.addAll(flattenedJson.keySet());

		}

		if (enclosedBy == null) {
			StringBuilder headerSb = new StringBuilder();

			for (String key : keys) {
				headerSb.append(key);
				headerSb.append(delimiter);
			}
			
			int end = 0;
			
			if(headerSb.length()>1){
				end = headerSb.length() - 1;
			}

			output.put(headerSb.substring(0, end).toString());

			for (int i = 0; i < flattenedJsonArray.length(); i++) {

				StringBuilder bodySb = new StringBuilder();

				for (String key : keys) {
					if (flattenedJsonArray.optJSONObject(i).has(key)) {
						bodySb.append(flattenedJsonArray.optJSONObject(i).opt(key));
					} else {
						bodySb.append("");
					}
					bodySb.append(delimiter);
				}

				end = 0;
				
				if(bodySb.length()>1){
					end = bodySb.length() - 1;
				}
				
				output.put(bodySb.substring(0, end).toString());
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
			
			if(headerSb.length()>1){
				end = headerSb.length() - 1;
			}
			
			output.put(headerSb.substring(0, end).toString());

			for (int i = 0; i < flattenedJsonArray.length(); i++) {

				StringBuilder bodySb = new StringBuilder();

				for (String key : keys) {
					bodySb.append(enclosedBy);
					if (flattenedJsonArray.optJSONObject(i).has(key)) {
						bodySb.append(flattenedJsonArray.optJSONObject(i).opt(key));
					} else {
						bodySb.append("");
					}
					bodySb.append(enclosedBy);
					bodySb.append(delimiter);
				}
				
				end = 0;
				
				if(bodySb.length()>1){
					end = bodySb.length() - 1;
				}
				
				output.put(bodySb.substring(0, end).toString());
			}

		}

		return output;

	}

	private static void concatenateJsonObject(JSONObject destination, JSONObject source) {
		for (String elementKey : source.keySet()) {
			destination.put(elementKey, source.opt(elementKey));
		}
	}

	private static JSONObject analyse(String baseKey, Object inputObj) {

		JSONObject output = new JSONObject();

		if (inputObj instanceof JSONObject) {

			JSONObject jsonObject = (JSONObject) inputObj;

			for (String key : jsonObject.keySet()) {
				Object value = jsonObject.opt(key);

				concatenateJsonObject(output, analyse(baseKey + "." + key, value));
			}

		} else if (inputObj instanceof JSONArray) {

			JSONArray inputArray = (JSONArray) inputObj;

			for (int i = 0; i < inputArray.length(); i++) {
				Object elementObj = inputArray.opt(i);
				concatenateJsonObject(output, analyse(baseKey + "[" + i + "]", elementObj));
				// if(elementObj instanceof JSONArray || elementObj instanceof
				// JSONObject){
				// analyse(baseKey + "." + i, inputObj);
				// } else if (elementObj instanceof String){
				//
				// }
			}

		} else {
			output.put(baseKey, inputObj);
		}

		return output;

	}

}
