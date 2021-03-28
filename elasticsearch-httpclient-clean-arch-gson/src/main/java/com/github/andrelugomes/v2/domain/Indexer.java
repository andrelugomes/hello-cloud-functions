package com.github.andrelugomes.v2.domain;

import com.github.andrelugomes.v2.domain.usecase.indexing.gateway.MerchantGateway;
import com.github.andrelugomes.v2.domain.usecase.indexing.gateway.EventParserGateway;
import com.github.andrelugomes.v2.domain.usecase.indexing.entity.Merchant;
import com.github.andrelugomes.v2.domain.usecase.indexing.entity.Event;

import java.io.IOException;
import java.util.logging.Logger;

public class Indexer {

    private static final Logger logger = Logger.getLogger(Indexer.class.getName());
    private static final String DELETE = "DELETE";
    private static final String UPSERT = "UPSERT";

    private MerchantGateway merchantGateway;
    private EventParserGateway eventParserGateway;

    public Indexer(MerchantGateway merchantGateway, EventParserGateway eventParserGateway) {
        this.merchantGateway = merchantGateway;
        this.eventParserGateway = eventParserGateway;
    }

    public void handle(final Event event) throws IOException, InterruptedException {
        if (event == null || event.getData() == null) {
            logger.warning("No event data provided");
            return;
        }
        var merchant = eventParserGateway.parse(event);

        logger.config(String.format("Processing merchant, merchant=%s", merchant));
        index(merchant);
    }

    private void index(final Merchant merchant) throws IOException, InterruptedException {
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
