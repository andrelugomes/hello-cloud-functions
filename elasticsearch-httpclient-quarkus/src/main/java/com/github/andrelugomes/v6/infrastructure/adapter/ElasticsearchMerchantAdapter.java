package com.github.andrelugomes.v6.infrastructure.adapter;

import com.github.andrelugomes.v6.domain.usecase.indexing.gateway.MerchantGateway;
import com.github.andrelugomes.v6.infrastructure.adapter.exception.ResponseException;
import com.github.andrelugomes.v6.infrastructure.client.ElasticClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class ElasticsearchMerchantAdapter implements MerchantGateway {

    private static final Logger logger = Logger.getLogger(ElasticsearchMerchantAdapter.class.getName());

    @Inject
    @RestClient
    private ElasticClient elasticClient;

    public void upsert(final String merchantId, final String merchantData) {
        var response = elasticClient.upsert(merchantId, merchantData);
        if (response.getStatus() == 201 || response.getStatus() == 200) {
            logger.info(String.format("Merchant upserted, merchantId=%s", merchantId));
        } else {
            logger.warning(String.format("Something wrong, merchantId=%s status=%s response=%s", merchantId, response.getStatus(), response.readEntity(String.class)));
            throw new ResponseException("Something wrong while upserting merchant");
        }
    }

    public void delete(final String merchantId) {
        var response = elasticClient.delete(merchantId);
        if (response.getStatus() == 200 || response.getStatus() == 404) {
            logger.info(String.format("Merchant deleted, merchantId=%s", merchantId));
        } else {
            logger.warning(String.format("Something wrong, merchantId=%s status=%s response=%s", merchantId, response.getStatus(), response.readEntity(String.class)));
            throw new ResponseException("Something wrong while deleting merchant");
        }
    }
}
