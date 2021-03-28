package com.github.andrelugomes.v3.infrastructure.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.andrelugomes.v3.domain.usecase.indexing.gateway.EventParserGateway;
import com.github.andrelugomes.v3.domain.usecase.indexing.entity.Event;
import com.github.andrelugomes.v3.domain.usecase.indexing.entity.Merchant;

import java.util.Base64;

public class JacksonEventParserAdapter implements EventParserGateway {

    @Override
    public Merchant parse(Event event) {
        var data = new String(Base64.getMimeDecoder().decode(event.getData()));
        var mapper = new ObjectMapper();
        var merchant = new Merchant();
        try {
            var partial = mapper.readValue(data, PartialModel.class);
            merchant.setId(partial.id);
            merchant.setOp(partial.op);
            //merchant.setJson(mapper.writeValueAsString(partial.data));
            merchant.setJson(partial.data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return merchant;
    }



    private static class PartialModel {
        //private JsonNode data;
        @JsonDeserialize(using = RawJsonDeserializer.class)
        private String data = "";
        private String op;
        private String id;

        public void setData(String data) {
            this.data = data;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
