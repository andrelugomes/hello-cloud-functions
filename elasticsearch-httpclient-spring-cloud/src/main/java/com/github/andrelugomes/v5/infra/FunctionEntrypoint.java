package com.github.andrelugomes.v5.infra;

import com.github.andrelugomes.v5.domain.usecase.indexing.Indexer;
import com.github.andrelugomes.v5.infra.adapter.ElasticsearchMerchantAdapter;
import com.github.andrelugomes.v5.infra.parser.JacksonEventParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class FunctionEntrypoint {

    public static void main(String[] args) {
        SpringApplication.run(FunctionEntrypoint.class, args);
    }

    @Bean
    public Consumer<Event> pubSubFunction(@Autowired JacksonEventParser parser, @Autowired ElasticsearchMerchantAdapter elasticsearchMerchantAdapter) {

        return event -> {
            var indexer = new Indexer(elasticsearchMerchantAdapter);
            try {
                var merchant = parser.parse(event);
                indexer.index(merchant);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        };
    }
}
