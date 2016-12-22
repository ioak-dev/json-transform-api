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

import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.codesunday.proteus.core.processor.Flattener;
import com.codesunday.proteus.core.processor.Transformer;

/**
 * Client implementation.
 * 
 * @author Arun Kumar Selvaraj
 *
 */
class ProteusClientImpl {

	private static ObjectMapper mapper = new ObjectMapper();

	ProteusClientImpl() {
		super();
	}

	ObjectNode transformImpl(ObjectNode input, String templateText) {

		ObjectNode template = null;

		try {
			template = Transformer.transformImpl(input, (ObjectNode) mapper.readTree(templateText));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return template;
	}

	public ObjectNode flattenAsJson(ObjectNode input) {
		return Flattener.flattenAsJson(input);
	}

	public ArrayNode flattenAsJson(ArrayNode input) {
		return Flattener.flattenAsJson(input);
	}

	public ArrayNode flattenAsDelimited(ObjectNode input, String delimiter, String enclosedBy) {
		return Flattener.flattenAsDelimited(input, delimiter, enclosedBy);
	}

	public ArrayNode flattenAsDelimited(ArrayNode input, String delimiter, String enclosedBy) {
		return Flattener.flattenAsDelimited(input, delimiter, enclosedBy);
	}
}
