package com.wang.scaffold.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ArrayStringSerializer extends StdSerializer<String> {

	private static final long serialVersionUID = 1L;

	public ArrayStringSerializer() {
		this(null);
	}

	public ArrayStringSerializer(Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		ObjectMapper mapper = (ObjectMapper) gen.getCodec();
		String[] strs = mapper.readValue(value, String[].class);
		gen.writeArray(strs, 0, strs.length);
	}

}
