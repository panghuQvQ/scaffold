package com.wang.scaffold.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ArrayStringDeserializer extends StdDeserializer<String> {

	private static final long serialVersionUID = 1L;

	public ArrayStringDeserializer() {
		this(null);
	}

	public ArrayStringDeserializer(Class<String> t) {
		super(t);
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode tree = p.readValueAsTree();
		if (tree.isEmpty()) {
			return null;
		}
		String str = tree.toPrettyString();
		return str;
	}

}
