package com.github.andrelugomes.v5.domain.usecase.indexing.gateway;

import java.io.IOException;

public interface MerchantGateway {

    void upsert(final String merchantId, final String merchantData) throws IOException, InterruptedException;

    void delete(final String merchantId) throws IOException, InterruptedException;

}
