package com.github.andrelugomes.v1.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Merchant {
    private JsonObject data;
    private String op;
    private String id;

    public Merchant(String merchantString) {
        Gson gson = new Gson();
        Merchant merchant = gson.fromJson(merchantString, Merchant.class);
        this.data = merchant.data;
        this.op = merchant.op;
        this.id = merchant.id;
    }

    public String getData() {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

