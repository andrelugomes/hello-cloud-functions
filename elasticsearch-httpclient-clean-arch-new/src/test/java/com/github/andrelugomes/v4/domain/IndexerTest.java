package com.github.andrelugomes.v4.domain;

import com.github.andrelugomes.v4.domain.usecase.indexing.Indexer;
import com.github.andrelugomes.v4.domain.usecase.indexing.entity.Merchant;
import com.github.andrelugomes.v4.infrastructure.adapter.exception.ResponseException;
import com.github.andrelugomes.v4.domain.usecase.indexing.gateway.MerchantGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class IndexerTest {

    @Test
    public void shouldSkipMessageBecauseHasDifferentOperation() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);

        var merchant = new Merchant();
        merchant.setId("123");
        merchant.setOp("UNKNOWN");

        var indexer = new Indexer(gateway);

        indexer.index(merchant);

        verify(gateway, never()).upsert(anyString(), anyString());
        verify(gateway, never()).delete(anyString());
    }

    @Test
    public void shouldHandleMessageThatUpsertMerchant() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);

        var merchant = new Merchant();
        merchant.setId("345");
        merchant.setOp("UPSERT");
        merchant.setJson("{\n" +
                "    \"name\": \"Steakhouse\",\n" +
                "    \"location\": {\n" +
                "      \"lat\": -21.95840072631836,\n" +
                "      \"lon\": -47.98820114135742\n" +
                "    },\n" +
                "    \"payment_options\": [\n" +
                "      \"CASH\", \"CREDIT_CARD\"\n" +
                "    ],\n" +
                "    \"category\": \"STEAKHOUSE\",\n" +
                "    \"created_at\": 1585606656568\n" +
                "  }");

        var handler = new Indexer(gateway);

        handler.index(merchant);

        verify(gateway, atLeastOnce()).upsert(anyString(), anyString());
    }

    @Test
    public void shouldHandleMessageThatDeleteMerchant() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);

        var merchant = new Merchant();
        merchant.setId("345");
        merchant.setOp("DELETE");

        var indexer = new Indexer(gateway);

        indexer.index(merchant);

        verify(gateway, atLeastOnce()).delete(anyString());
    }

    @Test(expected = ResponseException.class)
    public void shouldThrowResponseException() throws IOException, InterruptedException {
        var gateway = mock(MerchantGateway.class);
        doThrow(ResponseException.class).when(gateway).upsert(anyString(), anyString());

        var merchant = new Merchant();
        merchant.setId("234");
        merchant.setOp("UPSERT");
        merchant.setJson("{\"id\":\"1234567890\"}");

        var indexer = new Indexer(gateway);

        indexer.index(merchant);
    }
}