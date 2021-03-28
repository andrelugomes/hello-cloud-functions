package com.github.andrelugomes.v2.infrastructure.adapter;

import com.github.andrelugomes.v2.infrastructure.adapter.exception.ResponseException;
import com.github.andrelugomes.v2.domain.usecase.indexing.gateway.MerchantGateway;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class ElasticsearchMerchantAdapter implements MerchantGateway {

    private static final Logger logger = Logger.getLogger(ElasticsearchMerchantAdapter.class.getName());
    private static final String AUTHORIZATION = "Authorization";
    private static final String ELASTIC_ENDPOINT = System.getenv("ELASTIC_HOST") + ":" + System.getenv("ELASTIC_PORT");
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String MERCHANTS_DOC = "/merchants/_doc/";

    private HttpClient httpClient;
    private String secret;

    public ElasticsearchMerchantAdapter(HttpClient httpClient, String secret) {
        this.httpClient = httpClient;
        this.secret = secret;
    }

    public void upsert(final String merchantId, final String merchantData) throws IOException, InterruptedException {
        var response = httpClient.send(getUpsertRequest(merchantId, merchantData), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            logger.info(String.format("Merchant upserted, merchantId=%s", merchantId));
        } else {
            logger.warning(String.format("Something wrong, merchantId=%s status=%s response=%s", merchantId, response.statusCode(), response.body()));
            throw new ResponseException("Something wrong while upserting merchant");
        }
    }

    public void delete(final String merchantId) throws IOException, InterruptedException {
        var response = httpClient.send(getDeleteRequest(merchantId), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 404) {
            logger.info(String.format("Merchant deleted, merchantId=%s", merchantId));
        } else {
            logger.warning(String.format("Something wrong, merchantId=%s status=%s response=%s", merchantId, response.statusCode(), response.body()));
            throw new ResponseException("Something wrong while deleting merchant");
        }
    }

    private HttpRequest getUpsertRequest(final String merchantId, final String body) {
        return getRequest(HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(body)).setHeader(CONTENT_TYPE, APPLICATION_JSON), merchantId);
    }

    private HttpRequest getDeleteRequest(final String merchantId) {
        return getRequest(HttpRequest.newBuilder().DELETE(), merchantId);
    }

    private HttpRequest getRequest(HttpRequest.Builder httpRequest, final String merchantId) {
        httpRequest = httpRequest.uri(URI.create(ELASTIC_ENDPOINT + MERCHANTS_DOC + merchantId));

        if (!secret.isBlank()) {
            httpRequest = httpRequest.setHeader(AUTHORIZATION, getAuthentication(secret));
        }
        return httpRequest.build();
    }

    private String getAuthentication(String secret) {
        return "Basic " + secret;
    }
}
