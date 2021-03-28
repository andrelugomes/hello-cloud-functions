package com.github.andrelugomes.v5.domain.usecase.indexing;

import com.github.andrelugomes.v5.domain.usecase.indexing.entity.Merchant;
import com.github.andrelugomes.v5.domain.usecase.indexing.gateway.MerchantGateway;

import java.io.IOException;
import java.util.logging.Logger;

public class Indexer {

    private static final Logger logger = Logger.getLogger(Indexer.class.getName());
    private static final String DELETE = "DELETE";
    private static final String UPSERT = "UPSERT";

    private final MerchantGateway merchantGateway;

    public Indexer(MerchantGateway merchantGateway) {
        this.merchantGateway = merchantGateway;
    }

    public void index(final Merchant merchant) throws IOException, InterruptedException {
        try {
            if (DELETE.equals(merchant.getOp())) {
                merchantGateway.delete(merchant.getId());
            } else if (UPSERT.equals(merchant.getOp())) {
                merchantGateway.upsert(merchant.getId(), merchant.getJson());
            }
        } catch (final Exception ex) {
            logger.severe("Error while indexing merchant");
            throw ex;
        }
    }
}
