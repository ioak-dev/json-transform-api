package com.codesunday.proteus.core.examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.codesunday.proteus.core.client.ProteusClient;

public class Example {

	private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {

		ProteusClient client = ProteusClient.getInstance();

		ObjectNode outputObject = singleDocumentFlatteningExample(client);

		// System.out.println(
		// "*********Flattening As JSON Example - Single Document Demo - using
		// ObjectNode (Basic)*********");
		// System.out.println(outputObject.toString());

		ArrayNode outputArray = multiDocumentExample(client);

		// System.out.println(
		// "*********Flattening As JSON Example - Multi Document Demo - using
		// ArrayNode (Basic)*********");
		// System.out.println(outputArray.toString());
		//
		outputArray = singleDocumentFlatteningDelimitedExample(client, null, null);

		System.out.println(
				"*********Flattening As Delimited Example (Defaut delimiter) - Single Document Demo - using ObjectNode (Basic)*********");
		System.out.println(outputArray.toString());

		outputArray = singleDocumentFlatteningDelimitedExample(client, "|", "\"");

		System.out.println(
				"*********Flattening As Delimited Example (Custom delimiter) - Single Document Demo - using ObjectNode (Basic)*********");
		System.out.println(outputArray.toString());

		outputArray = multiDocumentFlatteningDelimitedExample(client, null, null);

		System.out.println(
				"*********Flattening As Delimited Example (Defaut delimiter) - Multi Document Demo - using ObjectNode (Basic)*********");
		System.out.println(outputArray.toString());

		outputArray = multiDocumentFlatteningDelimitedExample(client, "|", "\"");

		System.out.println(
				"*********Flattening As Delimited Example (Custom delimiter) - Multi Document Demo - using ObjectNode (Basic)*********");
		System.out.println(outputArray.toString());

		outputObject = singleDocumentExample(client, "example-data/templates/example-template-basic.json");

		System.out.println("*********Single Document Demo - using ObjectNode (Basic)*********");
		System.out.println(outputObject.toString());

		outputArray = multiDocumentExample(client, "example-data/templates/example-template-basic.json");

		System.out.println("*********Multi Document Demo - using ArrayNode (Basic)*********");
		System.out.println(outputArray.toString());

		outputObject = singleDocumentExample(client, "example-data/templates/example-template-advanced.json");

		System.out.println("*********Single Document Demo - using ObjectNode (Advanced)*********");
		System.out.println(outputObject.toString());

		outputArray = multiDocumentExample(client, "example-data/templates/example-template-advanced.json");

		System.out.println("*********Multi Document Demo - using ArrayNode (Advanced)*********");
		System.out.println(outputArray.toString());

		List<ArrayNode> multipletemplateOutput = multiTemplateExample(client,
				"example-data/templates/example-template-basic.json",
				"example-data/templates/example-template-advanced.json");

		System.out.println("*********Multi Template Demo - using ArrayNode (Advanced)*********");
		for (ArrayNode json : multipletemplateOutput) {
			System.out.println(json.toString());
		}

	}

	/**
	 * Using single ObjectNode as input
	 * 
	 * @param client
	 * @return @
	 */
	private static ObjectNode singleDocumentExample(ProteusClient client, String templatePath) {

		ObjectNode inputJson = null;
		try {
			inputJson = (ObjectNode) mapper.readTree(getJsonTextFromFile("example-data/input/example-jsonobject.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String templateJsonText = getJsonTextFromFile(templatePath);

		ObjectNode output = client.transform(inputJson, templateJsonText);

		return output;

	}

	/**
	 * Using ArrayNode as input for multiple documents input
	 * 
	 * @param client
	 * @return @
	 */
	private static ArrayNode multiDocumentExample(ProteusClient client, String templatePath) {

		ArrayNode inputJson = null;
		try {
			inputJson = inputJson = (ArrayNode) mapper
					.readTree(getJsonTextFromFile("example-data/input/example-jsonarray.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String templateJsonText = getJsonTextFromFile(templatePath);

		ArrayNode output = client.transform(inputJson, templateJsonText);

		return output;

	}

	/**
	 * Using ArrayNode as input for multiple documents input
	 * 
	 * @param client
	 * @return @
	 */
	private static List<ArrayNode> multiTemplateExample(ProteusClient client, String... templatePath) {

		ArrayNode inputJson = null;
		try {
			inputJson = (ArrayNode) mapper.readTree(getJsonTextFromFile("example-data/input/example-jsonarray.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] templateJsonText = new String[templatePath.length];

		for (int i = 0; i < templatePath.length; i++) {
			templateJsonText[i] = getJsonTextFromFile(templatePath[i]);
		}

		List<ArrayNode> output = client.transform(inputJson, templateJsonText);

		return output;

	}

	/**
	 * Flattening Using single ObjectNode as input
	 * 
	 * @param client
	 * @return @
	 */
	private static ObjectNode singleDocumentFlatteningExample(ProteusClient client) {

		ObjectNode inputJson = null;
		try {
			inputJson = (ObjectNode) mapper.readTree(getJsonTextFromFile("example-data/input/example-jsonobject.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return client.flattenAsJson(inputJson);
	}

	/**
	 * Flattening Using ArrayNode as input for multiple documents input
	 * 
	 * @param client
	 * @return @
	 */
	private static ArrayNode multiDocumentExample(ProteusClient client) {

		ArrayNode inputJson = null;
		try {
			inputJson = (ArrayNode) mapper.readTree(getJsonTextFromFile("example-data/input/example-jsonarray.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayNode output = client.flattenAsJson(inputJson);

		return output;

	}

	/**
	 * Flattening Using single ObjectNode as input
	 * 
	 * @param client
	 * @return @
	 */
	private static ArrayNode singleDocumentFlatteningDelimitedExample(ProteusClient client, String delimiter,
			String enclosedBy) {

		ObjectNode inputJson = null;
		try {
			inputJson = (ObjectNode) mapper.readTree(getJsonTextFromFile("example-data/input/example-jsonobject.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return client.flattenAsDelimited(inputJson, delimiter, enclosedBy);
	}

	/**
	 * Flattening Using ArrayNode as input for multiple documents input
	 * 
	 * @param client
	 * @return @
	 */
	private static ArrayNode multiDocumentFlatteningDelimitedExample(ProteusClient client, String delimiter,
			String enclosedBy) {

		ArrayNode inputJson = null;
		try {
			inputJson = (ArrayNode) mapper.readTree(getJsonTextFromFile("example-data/input/example-jsonarray.json"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayNode output = client.flattenAsDelimited(inputJson, delimiter, enclosedBy);

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
