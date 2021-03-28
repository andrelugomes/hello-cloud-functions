package com.github.andrelugomes.v5.infra.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class RawJsonDeserializer extends JsonDeserializer {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        var mapper = (ObjectMapper) parser.getCodec();
        var node = mapper.readTree(parser);
        return node != null ? mapper.writeValueAsString(node) : "";
    }
}
