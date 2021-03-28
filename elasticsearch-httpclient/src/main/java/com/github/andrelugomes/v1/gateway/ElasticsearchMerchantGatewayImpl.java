package com.github.andrelugomes.v1.gateway;

import com.github.andrelugomes.v1.exception.ResponseException;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Logger;

public class ElasticsearchMerchantGatewayImpl {

    private static final Logger logger = Logger.getLogger(ElasticsearchMerchantGatewayImpl.class.getName());
    private static final String ELASTIC_AUTHENTICATION = getElasticSearchAuthorizationCode();
    private static final String AUTHORIZATION = "Authorization";
    private static final String ELASTIC_ENDPOINT = System.getenv("ELASTIC_HOST") + ":" + System.getenv("ELASTIC_PORT");
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String MERCHANTS_DOC = "/merchants/_doc/";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public void upsert(String merchantId, String merchantData) throws IOException, InterruptedException {
        var response = httpClient.send(getUpsertRequest(merchantId, merchantData), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            logger.info(String.format("Merchant upserted, merchantId=%s", merchantId));
        } else {
            logger.warning(String.format("Something wrong, merchantId=%s status=%s response=%s", merchantId, response.statusCode(), response.body()));
            throw new ResponseException();
        }
    }

    public void delete(String merchantId) throws IOException, InterruptedException {
        var response = httpClient.send(getDeleteRequest(merchantId), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            logger.info(String.format("Merchant deleted, merchantId=%s", merchantId));
        } else {
            logger.warning(String.format("Something wrong, merchantId=%s status=%s response=%s", merchantId, response.statusCode(), response.body()));
        }
    }

    private HttpRequest getUpsertRequest(String merchantId, String body) {
        return getRequest(HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(body)).setHeader(CONTENT_TYPE, APPLICATION_JSON), merchantId);
    }

    private HttpRequest getDeleteRequest(String merchantId) {
        return getRequest(HttpRequest.newBuilder().DELETE(), merchantId);
    }

    private HttpRequest getRequest(HttpRequest.Builder httpRequest, String merchantId) {
        httpRequest = httpRequest.uri(URI.create(ELASTIC_ENDPOINT + MERCHANTS_DOC + merchantId));

        if (!ELASTIC_AUTHENTICATION.isBlank()) {
            httpRequest = httpRequest.setHeader(AUTHORIZATION, ELASTIC_AUTHENTICATION);
        }
        return httpRequest.build();
    }

    private static String getElasticSearchAuthorizationCode() {
        var projectId = System.getenv("GCP_PROJECT_NAME");
        var secretId = System.getenv("ELASTIC_SECRET_NAME");
        var versionId = System.getenv("ELASTIC_SECRET_VERSION");
        try {
            return "Basic "+ accessSecretVersion(projectId, secretId, versionId);
        } catch (Exception e) {
            logger.info(String.format("Error to retrieve elasticsearch authorization secret"));
            return "";
        }
    }

    private static String accessSecretVersion(String projectId, String secretId, String versionId) throws IOException {
        var client = SecretManagerServiceClient.create();
        var secretVersionName = SecretVersionName.of(projectId, secretId, versionId);
        var response = client.accessSecretVersion(secretVersionName);
        return response.getPayload().getData().toStringUtf8();
    }
}
