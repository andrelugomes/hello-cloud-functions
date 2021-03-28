package com.github.andrelugomes.v1;

import com.github.andrelugomes.v1.model.Event;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.github.andrelugomes.v1.model.Merchant;
import com.github.andrelugomes.v1.gateway.ElasticsearchMerchantGatewayImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.logging.Logger;

import static java.time.ZoneOffset.*;

public class FunctionEntrypoint implements BackgroundFunction<Event> {

    private static final Logger logger = Logger.getLogger(FunctionEntrypoint.class.getName());
    private static final long MAX_EVENT_AGE = 600_000;
    public static final String DELETE = "DELETE";
    public static final String UPSERT = "UPSERT";

    @Override
    public void accept(Event message, Context context) throws IOException, InterruptedException {
        if (message.getData() == null) {
            logger.warning("No message data provided");
            return;
        }

        var messageTime = ZonedDateTime.parse(context.timestamp());
        var eventAge = Duration.between(messageTime, ZonedDateTime.now(UTC)).toMillis();

        if (eventAge > MAX_EVENT_AGE) {
            logger.warning(String.format("Dropping old event, messageId=%s publishTime=%s.", message.getMessageId(), message.getPublishTime()));
            return;
        }

        var data = new String(Base64.getMimeDecoder().decode(message.getData()));

        logger.info(String.format("Processing message, data=%s", data));
        processMessage(data);
    }

    private void processMessage(final String message) throws IOException, InterruptedException {
        try {
            var merchant = new Merchant(message);
            if (DELETE.equals(merchant.getOp())) {
                new ElasticsearchMerchantGatewayImpl().delete(merchant.getId());
            } else if (UPSERT.equals(merchant.getOp())) {
                new ElasticsearchMerchantGatewayImpl().upsert(merchant.getId(), merchant.getData());
            }
        } catch (Exception ex) {
            logger.severe("Error while processing message");
            throw ex;
        }
    }
}
