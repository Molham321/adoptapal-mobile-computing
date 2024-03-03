package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.AuthCreateUserRequest
import de.fhe.adoptapal.model.AuthUserCreatedResponse
import io.quarkus.rest.client.reactive.NotBody
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 * REST client interface for communication with the authentication service.
 */
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
interface AuthServiceClient {
    /**
     * Creates a new user in the authentication service.
     *
     * @param request The [AuthCreateUserRequest] containing user details.
     * @return The [AuthUserCreatedResponse] with information about the created user.
     */
    @POST
    @Path("/user")
    fun create(request: AuthCreateUserRequest): AuthUserCreatedResponse

    /**
     * Deletes a user in the authentication service.
     *
     * @param userId The ID of the user to delete.
     * @param email The user's email for authentication.
     * @param password The user's password for authentication.
     */
    @DELETE
    @Path("/user/{userId}")
    @ClientHeaderParam(name = "X-User-Email", value = ["{email}"])
    @ClientHeaderParam(name = "X-User-Password", value = ["{password}"])
    fun delete(@PathParam("userId") userId: Long, @NotBody email: String, @NotBody password: String)

    /**
     * Validates the authenticity of a token with the authentication service.
     *
     * @param userId The ID of the user associated with the token.
     * @param token The authentication token to validate.
     */
    @GET
    @Path("/token/{userId}/validate")
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    fun isTokenValid(@PathParam("userId") userId: Long, @NotBody token: String)

    /**
     * Validates user credentials with the authentication service.
     *
     * @param userId The ID of the user for whom credentials are validated.
     * @param email The user's email for authentication.
     * @param password The user's password for authentication.
     */
    @GET
    @Path("/user/{userId}/validate")
    @ClientHeaderParam(name = "X-User-Email", value = ["{email}"])
    @ClientHeaderParam(name = "X-User-Password", value = ["{password}"])
    fun isCredentialsValid(@PathParam("userId") userId: Long, @NotBody email: String, @NotBody password: String)
}
