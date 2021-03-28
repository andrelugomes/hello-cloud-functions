package com.github.andrelugomes.v3.domain;

import com.github.andrelugomes.v3.domain.usecase.indexing.gateway.EventParserGateway;
import com.github.andrelugomes.v3.domain.usecase.indexing.entity.Event;
import com.github.andrelugomes.v3.infrastructure.adapter.JacksonEventParserAdapter;
import com.github.andrelugomes.v3.infrastructure.adapter.exception.ResponseException;
import com.github.andrelugomes.v3.domain.usecase.indexing.gateway.MerchantGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class IndexerTest {

    @Test
    public void shouldSkipMessageBecauseHasNoData() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);
        var parser = mock(EventParserGateway.class);

        Event event = new Event();
        event.setMessageId("ID");
        event.setPublishTime("TIMESTAMP");
        Indexer indexer = new Indexer(gateway, parser);

        indexer.handle(event);

        verify(gateway, never()).upsert(anyString(), anyString());
        verify(gateway, never()).delete(anyString());
        verify(parser, never()).parse(any(Event.class));
    }

    @Test
    public void shouldHandleMessageThatUpsertMerchant() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);
        var parser = new JacksonEventParserAdapter();

        Event event = new Event();
        event.setMessageId("ID");
        event.setPublishTime("TIMESTAMP");
        event.setData("ewogICJpZCI6IjEiLAogICJvcCI6IlVQU0VSVCIsCiAgImRhdGEiOiB7CiAgICAibmFtZSI6ICJTdGVha2hvdXNlIiwKICAg"
                +"ICJsb2NhdGlvbiI6IHsKICAgICAgImxhdCI6IC0yMS45NTg0MDA3MjYzMTgzNiwKICAgICAgImxvbiI6IC00Ny45ODgyMDExNDEzN"
                +"Tc0MgogICAgfSwKICAgICJwYXltZW50 X29wdGlvbnMiOiBbCiAgICAgICJDQVNIIiwgIkNSRURJVF9DQVJEIgogICAgXSwKICAgI"
                +"CJjYXRlZ29yeSI6ICJTVEVBS0hPVVNFIiwKICAgICJjcmVhdGVkX2F0IjogMTU4NTYwNjY1NjU2OAogIH0KfQ==");

        Indexer handler = new Indexer(gateway, parser);

        handler.handle(event);

        verify(gateway, atLeastOnce()).upsert(anyString(), anyString());
    }

    @Test
    public void shouldHandleMessageThatDeleteMerchant() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);
        var parser = new JacksonEventParserAdapter();

        Event event = new Event();
        event.setMessageId("ID");
        event.setPublishTime("TIMESTAMP");
        event.setData("eyJpZCI6IjMyMSIsIm9wIjoiREVMRVRFIn0="); //{"id":"321","op":"DELETE"}

        Indexer indexer = new Indexer(gateway, parser);

        indexer.handle(event);

        verify(gateway, atLeastOnce()).delete(anyString());
    }

    @Test(expected = ResponseException.class)
    public void shouldThrowResponseException() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);
        var parser = new JacksonEventParserAdapter();
        doThrow(ResponseException.class).when(gateway).upsert(anyString(), anyString());

        Event event = new Event();
        event.setMessageId("ID");
        event.setPublishTime("TIMESTAMP");
        event.setData("eyJpZCI6IjMyMSIsIm9wIjoiVVBTRVJUIn0="); //{"id":"321","op":"UPSERT"}

        Indexer indexer = new Indexer(gateway, parser);

        indexer.handle(event);
    }
}