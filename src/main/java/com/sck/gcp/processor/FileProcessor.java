package com.sck.gcp.processor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

	public JSONArray convertToJSONs(String xmlString) {
		JSONArray jsonProducts = null;
		try {
			JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
			jsonProducts = xmlJSONObj.getJSONObject("Products").getJSONArray("Product");
		} catch (JSONException je) {
			LOGGER.error("JSONException occurred on converting to json: ", je);
		}
		return jsonProducts;
	}

	public String convertToJSONL(JSONArray jsonProducts) {
		StringBuilder builder = new StringBuilder();
		String jsonPrettyPrintString = null;
		jsonProducts.forEach(i -> builder.append(i.toString()).append("\n"));
		jsonPrettyPrintString = builder.toString();
		LOGGER.info(jsonPrettyPrintString);
		return jsonPrettyPrintString;
	}
}
