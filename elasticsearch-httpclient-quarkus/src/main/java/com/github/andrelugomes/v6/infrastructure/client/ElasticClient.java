package com.github.andrelugomes.v6.infrastructure.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/merchants/_doc")
@RegisterRestClient(configKey="elasticsearch-api")
@RegisterClientHeaders(RequestAuthorizationHeaderFactory.class)
public interface ElasticClient {

    @PUT
    @Path("/{merchantId}")
    @Produces("application/json")
    Response upsert(@PathParam("merchantId") String merchantId, String merchantData);


    @DELETE
    @Path("/{merchantId}")
    Response delete(@PathParam("merchantId") String merchantId);
}
