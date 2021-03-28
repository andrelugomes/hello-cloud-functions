package com.github.andrelugomes.v2.infrastructure.adapter;

import com.github.andrelugomes.v2.domain.usecase.indexing.gateway.EventParserGateway;
import com.github.andrelugomes.v2.domain.usecase.indexing.entity.Event;
import com.github.andrelugomes.v2.domain.usecase.indexing.entity.Merchant;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Base64;

public class GsonEventParserAdapter implements EventParserGateway {

    @Override
    public Merchant parse(Event event) {
        var data = new String(Base64.getMimeDecoder().decode(event.getData()));
        var gson = new GsonBuilder().create();

        var partial = gson.fromJson(data, PartialModel.class);

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
