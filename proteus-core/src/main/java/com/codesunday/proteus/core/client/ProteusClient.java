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

package com.codesunday.proteus.core.client;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Client instance that will act as interface for this library.
 * 
 * @author Arun Kumar Selvaraj
 *
 */
public class ProteusClient {

	private static ObjectMapper mapper = new ObjectMapper();

	private ProteusClientImpl clientImpl;

	private ProteusClient() {

		super();

		clientImpl = new ProteusClientImpl();

	}

	public static ProteusClient getInstance() {

		ProteusClient client = new ProteusClient();

		return client;

	}

	/**
	 * Endpoint method for transforming a Json object based on a template text.
	 * 
	 * @param input
	 * @param templateText
	 * @return
	 */
	public ObjectNode transform(ObjectNode input, String templateText) {
		return clientImpl.transformImpl(input, templateText);
	}

	/**
	 * Endpoint method for transforming a Json object based on a Json template.
	 * 
	 * @param input
	 * @param template
	 * @return
	 */
	public ObjectNode transform(ObjectNode input, ObjectNode template) {
		return transform(input, template.toString());
	}

	/**
	 * Endpoint method for transforming list of Json objects based on a template
	 * text.
	 * 
	 * @param input
	 * @param templateText
	 * @return
	 */
	public List<ObjectNode> transform(List<ObjectNode> input, String templateText) {

		List<ObjectNode> output = new ArrayList();

		for (ObjectNode item : input) {
			output.add(transform(item, templateText));
		}

		return output;
	}

	/**
	 * Endpoint method for transforming list of Json objects based on a Json
	 * template.
	 * 
	 * @param input
	 * @param template
	 * @return
	 */
	public List<ObjectNode> transform(List<ObjectNode> input, ObjectNode template) {
		return transform(input, template.toString());
	}

	/**
	 * Endpoint method for transforming a Json array based on a template text.
	 * 
	 * @param input
	 * @param templateText
	 * @return
	 */
	public ArrayNode transform(ArrayNode input, String templateText) {

		ArrayNode output = mapper.createArrayNode();

		for (JsonNode node : input) {
			output.add(transform((ObjectNode) node, templateText));
		}

		return output;
	}

	/**
	 * Endpoint method for transforming a Json array based on a Json template.
	 * 
	 * @param input
	 * @param template
	 * @return
	 */
	public ArrayNode transform(ArrayNode input, ObjectNode template) {
		return transform(input, template.toString());
	}

	/**
	 * This method is for getting better performance when you want the input
	 * json to be transform into several different forms. Order of template
	 * texts given should be known by the caller, to be able to retrieve the
	 * output back in the same order.
	 * 
	 * Endpoint method for transforming list of Json objects based on a template
	 * text.
	 * 
	 * @param input
	 * @param templateText
	 * @return
	 */
	public List<List<ObjectNode>> transform(List<ObjectNode> input, String... templateText) {

		List<List<ObjectNode>> output = new ArrayList();

		for (int i = 0; i < templateText.length; i++) {
			List<ObjectNode> list = new ArrayList();
			output.add(list);
		}

		for (ObjectNode item : input) {
			for (int i = 0; i < templateText.length; i++) {
				output.get(i).add(transform(item, templateText[i]));
			}
		}

		return output;
	}

	/**
	 * This method is for getting better performance when you want the input
	 * json to be transform into several different forms. Order of template
	 * texts given should be known by the caller, to be able to retrieve the
	 * output back in the same order.
	 * 
	 * Endpoint method for transforming list of Json objects based on a Json
	 * template.
	 * 
	 * @param input
	 * @param template
	 * @return
	 */
	public List<List<ObjectNode>> transform(List<ObjectNode> input, ObjectNode... template) {

		String[] templateText = new String[template.length];

		for (int i = 0; i < template.length; i++) {
			templateText[i] = template[i].toString();
		}

		return transform(input, templateText);
	}

	/**
	 * This method is for getting better performance when you want the input
	 * json to be transform into several different forms. Order of template
	 * texts given should be known by the caller, to be able to retrieve the
	 * output back in the same order.
	 * 
	 * Endpoint method for transforming a Json array based on a template text.
	 * 
	 * @param input
	 * @param templateText
	 * @return
	 */
	public List<ArrayNode> transform(ArrayNode input, String... templateText) {

		List<ArrayNode> output = new ArrayList();

		for (int i = 0; i < templateText.length; i++) {
			ArrayNode jsonarray = mapper.createArrayNode();
			output.add(jsonarray);
		}

		for (JsonNode node : input) {
			for (int j = 0; j < templateText.length; j++) {
				output.get(j).add(transform((ObjectNode) node, templateText[j]));
			}
		}

		return output;
	}

	/**
	 * This method is for getting better performance when you want the input
	 * json to be transform into several different forms. Order of template
	 * texts given should be known by the caller, to be able to retrieve the
	 * output back in the same order.
	 * 
	 * Endpoint method for transforming a Json array based on a Json template.
	 * 
	 * @param input
	 * @param template
	 * @return
	 */
	public List<ArrayNode> transform(ArrayNode input, ObjectNode... template) {

		String[] templateText = new String[template.length];

		for (int i = 0; i < template.length; i++) {
			templateText[i] = template[i].toString();
		}

		return transform(input, templateText);
	}

	public ObjectNode flattenAsJson(ObjectNode input) {
		return clientImpl.flattenAsJson(input);
	}

	public ArrayNode flattenAsJson(ArrayNode input) {
		return clientImpl.flattenAsJson(input);
	}

	public ArrayNode flattenAsDelimited(ObjectNode input, String delimiter, String enclosedBy) {
		return clientImpl.flattenAsDelimited(input, delimiter, enclosedBy);
	}

	public ArrayNode flattenAsDelimited(ArrayNode input, String delimiter, String enclosedBy) {
		return clientImpl.flattenAsDelimited(input, delimiter, enclosedBy);
	}

}
