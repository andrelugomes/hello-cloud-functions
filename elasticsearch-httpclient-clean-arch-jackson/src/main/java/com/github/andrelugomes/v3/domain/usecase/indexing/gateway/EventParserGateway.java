package com.github.andrelugomes.v3.domain.usecase.indexing.gateway;

import com.github.andrelugomes.v3.domain.usecase.indexing.entity.Merchant;
import com.github.andrelugomes.v3.domain.usecase.indexing.entity.Event;

public interface EventParserGateway {

    Merchant parse(Event data);
}
