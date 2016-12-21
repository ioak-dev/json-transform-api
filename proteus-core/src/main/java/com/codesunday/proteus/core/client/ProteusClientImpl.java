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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codesunday.proteus.core.processor.Flattener;
import com.codesunday.proteus.core.processor.Transformer;

/**
 * Client implementation.
 * 
 * @author Arun Kumar Selvaraj
 *
 */
class ProteusClientImpl {

	ProteusClientImpl() {
		super();
	}

	JSONObject transformImpl(JSONObject input, String templateText) {

		JSONObject template = null;

		try {

			template = Transformer.transformImpl(input, new JSONObject(templateText));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return template;
	}

	public JSONObject flattenAsJson(JSONObject input) {
		return Flattener.flattenAsJson(input);
	}

	public JSONArray flattenAsJson(JSONArray input) {
		return Flattener.flattenAsJson(input);
	}

	public JSONArray flattenAsDelimited(JSONObject input, String delimiter, String enclosedBy) {
		return Flattener.flattenAsDelimited(input, delimiter, enclosedBy);
	}

	public JSONArray flattenAsDelimited(JSONArray input, String delimiter, String enclosedBy) {
		return Flattener.flattenAsDelimited(input, delimiter, enclosedBy);
	}
}
