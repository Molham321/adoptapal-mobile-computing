package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.TokenBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import de.fhe.adoptapal.model.NewTokenResponse
import de.fhe.adoptapal.model.UserCredentials
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
@Consumes(MediaType.APPLICATION_JSON)
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
    fun createForUser(@PathParam("userId") userId: Long, @BeanParam credentials: UserCredentials): Response {
        return try {
            tokenBean.validateCredentials(credentials, userId)
            val token = tokenBean.createForUser(userId, credentials.email)
            Response.ok(NewTokenResponse(token.token.id!!, token.tokenString, token.token.expiresAt)).build()
        } catch (e: Exception) {
            LOG.info("failed to generate new token for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{userId}/{id}")
    @Authenticated
    fun getForUser(@PathParam("userId") userId: Long, @PathParam("id") id: Long): Response {
        return try {
            tokenBean.validateCredentials(jwt, userId)
            val token = tokenBean.getForUser(userId, id)
            Response.ok(token).build()
        } catch (e: Exception) {
            LOG.error("failed to list tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{userId}/all")
    @Authenticated
    fun getAllForUser(@PathParam("userId") userId: Long): Response {
        return try {
            tokenBean.validateCredentials(jwt, userId)
            val tokens = tokenBean.getAllForUser(userId)
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
    fun deleteForUser(@PathParam("userId") userId: Long, @PathParam("id") id: Long, @BeanParam credentials: UserCredentials): Response {
        return try {
            tokenBean.validateCredentials(credentials, userId)
            tokenBean.deleteForUser(userId, id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @DELETE
    @Path("/{userId}/all")
    fun deleteAllForUser(@PathParam("userId") userId: Long, @BeanParam credentials: UserCredentials): Response {
        return try {
            tokenBean.validateCredentials(credentials, userId)
            tokenBean.deleteAllForUser(userId)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete tokens for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{userId}/validate")
    @Authenticated
    fun validate(@PathParam("userId") userId: Long): Response {
        return try {
            tokenBean.validateCredentials(jwt, userId)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed validation of token for user with email `$userId`", e)
            mapExceptionToResponse(e)
        }
    }
}
