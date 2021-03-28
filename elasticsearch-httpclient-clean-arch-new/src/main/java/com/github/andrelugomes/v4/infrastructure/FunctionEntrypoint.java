package com.github.andrelugomes.v4.infrastructure;

import com.github.andrelugomes.v4.domain.usecase.indexing.Indexer;
import com.github.andrelugomes.v4.infrastructure.adapter.ElasticsearchMerchantAdapter;
import com.github.andrelugomes.v4.infrastructure.parser.JacksonEventParser;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.logging.Logger;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.time.Duration.ofSeconds;

public class FunctionEntrypoint implements BackgroundFunction<Event> {

    private static final Logger logger = Logger.getLogger(FunctionEntrypoint.class.getName());
    private static final String ELASTIC_AUTHENTICATION = getSecret();

    @Override
    public void accept(Event event, Context context) throws IOException, InterruptedException {
        var httpClient = HttpClient.newBuilder().version(HTTP_1_1).connectTimeout(ofSeconds(10)).build();
        var merchantAdapter = new ElasticsearchMerchantAdapter(httpClient, ELASTIC_AUTHENTICATION);
        var merchantParserAdapter = new JacksonEventParser();
        var indexer = new Indexer(merchantAdapter);

        var merchant = merchantParserAdapter.parse(event);
        indexer.index(merchant);
    }

    private static String getSecret() {
        var projectId = System.getenv("GCP_PROJECT_NAME");
        var secretId = System.getenv("ELASTIC_SECRET_NAME");
        var versionId = System.getenv("ELASTIC_SECRET_VERSION");
        try {
            var client = SecretManagerServiceClient.create();
            var secretVersionName = SecretVersionName.of(projectId, secretId, versionId);
            var response = client.accessSecretVersion(secretVersionName);
            return response.getPayload().getData().toStringUtf8();
        } catch (Exception e) {
            logger.warning(String.format("Error to retrieve elasticsearch authorization secret"));
            return "";
        }
    }
}
