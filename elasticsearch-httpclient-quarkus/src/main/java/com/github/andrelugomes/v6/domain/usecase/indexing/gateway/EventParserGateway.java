package com.github.andrelugomes.v6.domain.usecase.indexing.gateway;

import com.github.andrelugomes.v6.domain.usecase.indexing.entity.Event;
import com.github.andrelugomes.v6.domain.usecase.indexing.entity.Merchant;

public interface EventParserGateway {

    Merchant parse(Event data);
}
