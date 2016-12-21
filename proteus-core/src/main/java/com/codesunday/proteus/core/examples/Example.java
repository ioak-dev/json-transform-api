package com.codesunday.proteus.core.examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codesunday.proteus.core.client.ProteusClient;

public class Example {

	public static void main(String[] args) throws JSONException {

		ProteusClient client = ProteusClient.getInstance();

		JSONObject outputObject = singleDocumentFlatteningExample(client);

		// System.out.println(
		// "*********Flattening As JSON Example - Single Document Demo - using
		// JSONObject (Basic)*********");
		// System.out.println(outputObject.toString(3));

		JSONArray outputArray = multiDocumentExample(client);

		// System.out.println(
		// "*********Flattening As JSON Example - Multi Document Demo - using
		// JSONArray (Basic)*********");
		// System.out.println(outputArray.toString(3));
		//
		outputArray = singleDocumentFlatteningDelimitedExample(client, null, null);

		System.out.println(
				"*********Flattening As Delimited Example (Defaut delimiter) - Single Document Demo - using JSONObject (Basic)*********");
		System.out.println(outputArray.toString(3));

		outputArray = singleDocumentFlatteningDelimitedExample(client, "|", "\"");

		System.out.println(
				"*********Flattening As Delimited Example (Custom delimiter) - Single Document Demo - using JSONObject (Basic)*********");
		System.out.println(outputArray.toString(3));

		outputArray = multiDocumentFlatteningDelimitedExample(client, null, null);

		System.out.println(
				"*********Flattening As Delimited Example (Defaut delimiter) - Multi Document Demo - using JSONObject (Basic)*********");
		System.out.println(outputArray.toString(3));

		outputArray = multiDocumentFlatteningDelimitedExample(client, "|", "\"");

		System.out.println(
				"*********Flattening As Delimited Example (Custom delimiter) - Multi Document Demo - using JSONObject (Basic)*********");
		System.out.println(outputArray.toString(3));

		outputObject = singleDocumentExample(client, "example-data/templates/example-template-basic.json");

		System.out.println("*********Single Document Demo - using JSONObject (Basic)*********");
		System.out.println(outputObject.toString(3));

		outputArray = multiDocumentExample(client, "example-data/templates/example-template-basic.json");

		System.out.println("*********Multi Document Demo - using JSONArray (Basic)*********");
		System.out.println(outputArray.toString(3));

		outputObject = singleDocumentExample(client, "example-data/templates/example-template-advanced.json");

		System.out.println("*********Single Document Demo - using JSONObject (Advanced)*********");
		System.out.println(outputObject.toString(3));

		outputArray = multiDocumentExample(client, "example-data/templates/example-template-advanced.json");

		System.out.println("*********Multi Document Demo - using JSONArray (Advanced)*********");
		System.out.println(outputArray.toString(3));

		List<JSONArray> multipletemplateOutput = multiTemplateExample(client,
				"example-data/templates/example-template-basic.json",
				"example-data/templates/example-template-advanced.json");

		System.out.println("*********Multi Template Demo - using JSONArray (Advanced)*********");
		for (JSONArray json : multipletemplateOutput) {
			System.out.println(json.toString(3));
		}

	}

	/**
	 * Using single JSONObject as input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject singleDocumentExample(ProteusClient client, String templatePath) throws JSONException {

		JSONObject inputJson = new JSONObject(getJsonTextFromFile("example-data/input/example-jsonobject.json"));

		String templateJsonText = getJsonTextFromFile(templatePath);

		JSONObject output = client.transform(inputJson, templateJsonText);

		return output;

	}

	/**
	 * Using JSONArray as input for multiple documents input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray multiDocumentExample(ProteusClient client, String templatePath) throws JSONException {

		JSONArray inputJson = new JSONArray(getJsonTextFromFile("example-data/input/example-jsonarray.json"));

		String templateJsonText = getJsonTextFromFile(templatePath);

		JSONArray output = client.transform(inputJson, templateJsonText);

		return output;

	}

	/**
	 * Using JSONArray as input for multiple documents input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static List<JSONArray> multiTemplateExample(ProteusClient client, String... templatePath)
			throws JSONException {

		JSONArray inputJson = new JSONArray(getJsonTextFromFile("example-data/input/example-jsonarray.json"));

		String[] templateJsonText = new String[templatePath.length];

		for (int i = 0; i < templatePath.length; i++) {
			templateJsonText[i] = getJsonTextFromFile(templatePath[i]);
		}

		List<JSONArray> output = client.transform(inputJson, templateJsonText);

		return output;

	}

	/**
	 * Flattening Using single JSONObject as input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject singleDocumentFlatteningExample(ProteusClient client) throws JSONException {

		JSONObject inputJson = new JSONObject(getJsonTextFromFile("example-data/input/example-jsonobject.json"));

		return client.flattenAsJson(inputJson);
	}

	/**
	 * Flattening Using JSONArray as input for multiple documents input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray multiDocumentExample(ProteusClient client) throws JSONException {

		JSONArray inputJson = new JSONArray(getJsonTextFromFile("example-data/input/example-jsonarray.json"));

		JSONArray output = client.flattenAsJson(inputJson);

		return output;

	}

	/**
	 * Flattening Using single JSONObject as input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray singleDocumentFlatteningDelimitedExample(ProteusClient client, String delimiter,
			String enclosedBy) throws JSONException {

		JSONObject inputJson = new JSONObject(getJsonTextFromFile("example-data/input/example-jsonobject.json"));

		return client.flattenAsDelimited(inputJson, delimiter, enclosedBy);
	}

	/**
	 * Flattening Using JSONArray as input for multiple documents input
	 * 
	 * @param client
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray multiDocumentFlatteningDelimitedExample(ProteusClient client, String delimiter,
			String enclosedBy) throws JSONException {

		JSONArray inputJson = new JSONArray(getJsonTextFromFile("example-data/input/example-jsonarray.json"));

		JSONArray output = client.flattenAsDelimited(inputJson, delimiter, enclosedBy);

		return output;

	}

	private static String getJsonTextFromFile(String filepath) {

		String text = null;

		try {

			File file = new File(filepath);
			byte[] bytes = Files.readAllBytes(file.toPath());

			text = new String(bytes, "UTF-8");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}

}
