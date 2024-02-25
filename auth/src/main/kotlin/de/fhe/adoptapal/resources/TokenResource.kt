package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.TokenBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import de.fhe.adoptapal.model.NewTokenResponse
import de.fhe.adoptapal.model.RequestSubject
import de.fhe.adoptapal.model.TokenRequest
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@RequestScoped
@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
class TokenResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(TokenResource::class.java)
    }

    @Inject
    private lateinit var jwt: JsonWebToken

    @Inject
    private lateinit var tokenBean: TokenBean

    @GET
    @Path("/{userId}/new")
    fun createForUser(@PathParam("userId") userId: Long, request: TokenRequest): Response {
        return try {
            val token = tokenBean.createForUserAuthorized(RequestSubject(userId, request.email, request.password))
            Response.ok(NewTokenResponse(token.token.id!!, token.tokenString, token.token.expiresAt)).build()
        } catch (e: Exception) {
            LOG.info("failed to generate new token for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{userId}/{id}")
    fun getForUser(@PathParam("userId") userId: Long, @PathParam("id") id: Long, request: TokenRequest): Response {
        return try {
            val token = tokenBean.getForUserAuthorized(RequestSubject(userId, request.email, request.password), id)
            Response.ok(token).build()
        } catch (e: Exception) {
            LOG.error("failed to list tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{userId}/all")
    fun getAllForUser(@PathParam("userId") userId: Long, request: TokenRequest): Response {
        return try {
            val tokens = tokenBean.getAllForUserAuthorized(RequestSubject(userId, request.email, request.password))
            if (tokens.isNotEmpty()) {
                Response.ok(tokens).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to list tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @DELETE
    @Path("/{userId}/{id}")
    fun deleteForUser(@PathParam("userId") userId: Long, @PathParam("id") id: Long, request: TokenRequest): Response {
        return try {
            tokenBean.deleteForUserAuthorized(RequestSubject(userId, request.email, request.password), id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @DELETE
    @Path("/{userId}/all")
    fun deleteAllForUser(@PathParam("userId") userId: Long, request: TokenRequest): Response {
        return try {
            tokenBean.deleteAllForUserAuthorized(RequestSubject(userId, request.email, request.password))
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/validate")
    @Authenticated
    fun validate(): Response {
        return try {
            tokenBean.validate(jwt)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed validation of token for user with email `${jwt.name}`", e)
            mapExceptionToResponse(e)
        }
    }
}
