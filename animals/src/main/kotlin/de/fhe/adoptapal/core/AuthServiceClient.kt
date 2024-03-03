package de.fhe.adoptapal.core

import io.quarkus.rest.client.reactive.NotBody
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 * REST client interface for communicating with the authentication service.
 */
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
interface AuthServiceClient {

    /**
     * Validates the provided token for a given user ID.
     *
     * @param userId The ID of the user.
     * @param token The authentication token to be validated.
     */
    @GET
    @Path("/token/{userId}/validate")
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    fun isTokenValid(@PathParam("userId") userId: Long, @NotBody token: String)

}