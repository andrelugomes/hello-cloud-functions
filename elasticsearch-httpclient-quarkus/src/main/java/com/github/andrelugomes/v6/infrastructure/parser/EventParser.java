package com.github.andrelugomes.v6.infrastructure.parser;

import com.github.andrelugomes.v6.domain.usecase.indexing.entity.Event;
import com.github.andrelugomes.v6.domain.usecase.indexing.entity.Merchant;
import com.github.andrelugomes.v6.domain.usecase.indexing.gateway.EventParserGateway;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import java.util.Base64;

@ApplicationScoped
public class EventParser implements EventParserGateway {

    public Merchant parse(Event event) {
        var data = new String(Base64.getMimeDecoder().decode(event.getData()));
        var gson = new GsonBuilder().create();

        var partial = gson.fromJson(data, EventParser.PartialModel.class);

        var merchant = new Merchant();
        merchant.setId(partial.id);
        merchant.setOp(partial.op);
        merchant.setJson(gson.toJson(partial.data));
        return merchant;
    }

    class PartialModel {
        private JsonObject data;
        private String op;
        private String id;
    }
}
