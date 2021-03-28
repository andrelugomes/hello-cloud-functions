package com.github.andrelugomes.v4.infrastructure.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.andrelugomes.v4.infrastructure.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JacksonEventParserTest {

    private JacksonEventParser parser;

    @Before
    public void before() {
        parser = new JacksonEventParser();
    }

    @Test
    public void shouldParseUpsertEvent() throws JsonProcessingException {
        var expectedJson = "{\"name\":\"Steakhouse\",\"location\":{\"lat\":-21.95840072631836,\"lon\":-47.98820114135742},\"payment_options\":[\"CASH\",\"CREDIT_CARD\"],\"category\":\"STEAKHOUSE\",\"created_at\":1585606656568}";

        var event = new Event();
        event.setMessageId("ID");
        event.setPublishTime("TIMESTAMP");
        event.setData("ewogICJpZCI6IjEiLAogICJvcCI6IlVQU0VSVCIsCiAgImRhdGEiOiB7CiAgICAibmFtZSI6ICJTdGVha2hvdXNlIiwKICAg"
                + "ICJsb2NhdGlvbiI6IHsKICAgICAgImxhdCI6IC0yMS45NTg0MDA3MjYzMTgzNiwKICAgICAgImxvbiI6IC00Ny45ODgyMDExNDEzN"
                + "Tc0MgogICAgfSwKICAgICJwYXltZW50 X29wdGlvbnMiOiBbCiAgICAgICJDQVNIIiwgIkNSRURJVF9DQVJEIgogICAgXSwKICAgI"
                + "CJjYXRlZ29yeSI6ICJTVEVBS0hPVVNFIiwKICAgICJjcmVhdGVkX2F0IjogMTU4NTYwNjY1NjU2OAogIH0KfQ==");


        var merchant = parser.parse(event);

        Assert.assertEquals("1", merchant.getId());
        Assert.assertEquals("UPSERT", merchant.getOp());
        Assert.assertEquals(expectedJson, merchant.getJson());
    }

    @Test
    public void shouldParseDeleteEvent() throws JsonProcessingException {
        var event = new Event();
        event.setMessageId("ID");
        event.setPublishTime("TIMESTAMP");
        event.setData("eyJpZCI6IjMyMSIsIm9wIjoiREVMRVRFIn0=");

        var merchant = parser.parse(event);

        Assert.assertEquals("321", merchant.getId());
        Assert.assertEquals("DELETE", merchant.getOp());
        Assert.assertEquals("", merchant.getJson());
    }

}