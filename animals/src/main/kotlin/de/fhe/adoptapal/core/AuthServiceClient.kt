package de.fhe.adoptapal.core

import io.quarkus.rest.client.reactive.NotBody
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
interface AuthServiceClient {
    @GET
    @Path("/token/{userId}/validate")
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    fun isTokenValid(@PathParam("userId") userId: Long, @NotBody token: String)

}