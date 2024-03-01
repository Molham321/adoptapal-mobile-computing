package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.AuthCreateUserRequest
import de.fhe.adoptapal.model.AuthUserCreatedResponse
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
    @POST
    @Path("/user")
    fun create(request: AuthCreateUserRequest): AuthUserCreatedResponse

    @DELETE
    @Path("/user/{userId}")
    @ClientHeaderParam(name = "X-User-Email", value = ["{email}"])
    @ClientHeaderParam(name = "X-User-Password", value = ["{password}"])
    fun delete(@PathParam("userId") userId: Long, @NotBody email: String, @NotBody password: String)

    @GET
    @Path("/token/{userId}/validate")
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    fun isTokenValid(@PathParam("userId") userId: Long, @NotBody token: String)

    @GET
    @Path("/user/{userId}/validate")
    @ClientHeaderParam(name = "X-User-Email", value = ["{email}"])
    @ClientHeaderParam(name = "X-User-Password", value = ["{password}"])
    fun isCredentialsValid(@PathParam("userId") userId: Long, @NotBody email: String, @NotBody password: String)
}
