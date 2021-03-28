package com.github.andrelugomes.v5.infra.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.andrelugomes.v5.domain.usecase.indexing.entity.Merchant;
import com.github.andrelugomes.v5.infra.Event;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JacksonEventParser {

    public Merchant parse(Event event) {
        var data = new String(Base64.getMimeDecoder().decode(event.getData()));
        var mapper = new ObjectMapper();
        var merchant = new Merchant();
        try {
            var partial = mapper.readValue(data, PartialModel.class);
            merchant.setId(partial.id);
            merchant.setOp(partial.op);
            merchant.setJson(partial.data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro de parser");
        }

        return merchant;
    }

    private static class PartialModel {
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
