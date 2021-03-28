package com.github.andrelugomes.v6.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.andrelugomes.v6.domain.usecase.indexing.Indexer;
import com.github.andrelugomes.v6.domain.usecase.indexing.entity.Event;
import com.github.andrelugomes.v6.infrastructure.adapter.ElasticsearchMerchantAdapter;
import com.github.andrelugomes.v6.infrastructure.parser.EventParser;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;

import java.io.IOException;

@Named("pubSubTrigger")
@ApplicationScoped
public class FunctionEntrypoint implements BackgroundFunction<Event> {

    @Inject
    private EventParser eventParser;

    @Inject
    private ElasticsearchMerchantAdapter elasticsearchMerchantAdapter;

    @Override
    public void accept(Event event, Context context) throws IOException, InterruptedException {
        System.out.println("Receive event: " + event);

        var indexer = new Indexer(elasticsearchMerchantAdapter, eventParser);
        indexer.handle(event);
    }
}