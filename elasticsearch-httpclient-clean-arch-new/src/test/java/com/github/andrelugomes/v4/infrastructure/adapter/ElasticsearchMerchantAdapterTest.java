package com.github.andrelugomes.v4.infrastructure.adapter;

import com.github.andrelugomes.v4.infrastructure.adapter.exception.ResponseException;
import com.pgssoft.httpclient.HttpClientMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ElasticsearchMerchantAdapterTest {

    private HttpClientMock httpClient;
    private ElasticsearchMerchantAdapter elasticsearchMerchantAdapter;

    @Rule
    public EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void setUp() {
        environmentVariables.set("ELASTIC_HOST", "http://localhost");
        environmentVariables.set("ELASTIC_PORT", "9200");
        httpClient = new HttpClientMock();
        elasticsearchMerchantAdapter = new ElasticsearchMerchantAdapter(httpClient, "SECRET");
    }

    @Test
    public void shouldReturnOkWhenUpsertMerchant() throws IOException, InterruptedException {
        httpClient.onPut().doReturn("OK");

        elasticsearchMerchantAdapter.upsert("MERCHANT_ID", "data");
    }

    @Test
    public void shouldReturnCreatedWhenUpsertMerchant() throws IOException, InterruptedException {
        httpClient.onPut().doReturn("CREATED").withStatus(201);

        elasticsearchMerchantAdapter.upsert("MERCHANT_ID", "data");
    }

    @Test(expected = ResponseException.class)
    public void shouldThrowExceptionTryingUpsert() throws IOException, InterruptedException {
        httpClient.onPut()
                .doReturn("{\"_index\":\"merchants\",\"_id\":\"123\",\"_version\":1,\"result\":\"ERROR\"}")
                .withStatus(500);

        elasticsearchMerchantAdapter.upsert("123", "data");
    }

    @Test
    public void shouldReturnOkWhenDeleteMerchant() throws IOException, InterruptedException {
        httpClient.onDelete().doReturn("OK");

        elasticsearchMerchantAdapter.delete("MERCHANT_ID");
    }

    @Test
    public void shouldReturnNotFoundWhenDeleteMerchant() throws IOException, InterruptedException {
        httpClient.onDelete().doReturn("NOT_FOUNT").withStatus(404);

        elasticsearchMerchantAdapter.delete("WRONG_ID");
    }

    @Test(expected = ResponseException.class)
    public void shouldThrowExceptionTryingDelete() throws IOException, InterruptedException {
        httpClient.onDelete()
                .doReturn("{\"_index\":\"merchants\",\"_id\":\"321\",\"_version\":1,\"result\":\"ERROR\"}")
                .withStatus(500);

        elasticsearchMerchantAdapter.delete("321");
    }

}