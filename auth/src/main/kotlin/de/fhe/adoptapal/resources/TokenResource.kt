package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.JwtBean
import de.fhe.adoptapal.core.PasswordUtils
import de.fhe.adoptapal.model.TokenRepository
import de.fhe.adoptapal.model.UserRepository
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
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var tokenRepository: TokenRepository

    @Inject
    private lateinit var jwtBean: JwtBean

    private fun validatePassword(id: Long, password: String): Response? {
        LOG.info("validating password for user with id `$id`")

        val user = userRepository.findById(id)
                ?: return Response.status(Response.Status.NOT_FOUND).entity("User with id `$id` not found").build()

        return if (PasswordUtils.verifyPassword(password, user.password)) {
            null
        } else {
            LOG.warn("failed password validation for user with id `${user.id}`")
            Response.status(Response.Status.UNAUTHORIZED).build()
        }
    }

    private fun validateTokenAccess(id: Long, userId: Long, password: String): Response? {
        LOG.info("validating token access for user with id `$userId`")

        val e = validatePassword(userId, password)
        if (e != null) {
            return e
        }

        val token = tokenRepository.findById(id)
        return if (token == null) {
            Response.status(Response.Status.NOT_FOUND).build()
        } else if (token.userId != userId) {
            LOG.error("token with id `$id` does not belong to user with id `$userId`")
            Response.serverError().build()
        } else {
            null
        }
    }

    @GET
    @Path("/{userId}/new")
    fun requestNewToken(@PathParam("userId") userId: Long, request: TokenRequest): Response {
        LOG.info("generating new token for user with id `$userId`")
        return try {
            val e = validatePassword(userId, request.password)
            if (e != null) {
                return e
            }

            val user = userRepository.findById(userId)!!
            val token = jwtBean.generateToken(user)
            Response.ok(NewTokenResponse(token.token.id!!, token.tokenString, token.token.expiresAt)).build()
        } catch (e: Exception) {
            LOG.info("failed to generate new token for user with id `$userId`")
            Response.serverError().build()
        }
    }

    @GET
    @Path("/{userId}/all")
    fun getTokens(@PathParam("userId") userId: Long, request: TokenRequest): Response {
        LOG.info("listing tokens for user with id `$userId`")
        return try {
            val e = validatePassword(userId, request.password)
            if (e != null) {
                return e
            }

            val tokens = tokenRepository.listAllForUser(userId)
            if (tokens.isNotEmpty()) {
                Response.ok(tokens).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to list tokens for user with id `$userId`", e)
            Response.serverError().build()
        }
    }

    @GET
    @Path("/{userId}/{id}")
    fun getToken(@PathParam("userId") userId: Long, @PathParam("id") id: Long, request: TokenRequest): Response {
        LOG.info("listing token with id `$id` for user with id `$userId`")
        return try {
            val e = validateTokenAccess(id, userId, request.password)
            if (e != null) {
                return e
            }

            val token = tokenRepository.findById(id)!!
            Response.ok(token).build()
        } catch (e: Exception) {
            LOG.error("failed to list tokens for user with id `$userId`", e)
            Response.serverError().build()
        }
    }

    @DELETE
    @Path("/{userId}/{id}")
    fun deleteToken(@PathParam("userId") userId: Long, @PathParam("id") id: Long, request: TokenRequest): Response {
        LOG.info("deleting token with id `$id` for user with id `$userId`")
        return try {
            val e = validateTokenAccess(id, userId, request.password)
            if (e != null) {
                return e
            }

            tokenRepository.delete(id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete tokens for user with id `$userId`", e)
            Response.serverError().build()
        }
    }

    @GET
    @Path("/validate")
    @Authenticated
    fun validateToken(): Response {
        LOG.info("validating token for user with email `${jwt.name}`")
        return try {
            if (jwtBean.validate(jwt)) {
                Response.ok().build()
            } else {
                Response.status(Response.Status.UNAUTHORIZED).build()
            }
        } catch (e: Exception) {
            LOG.error("failed validation of token for user with email `${jwt.name}`", e)
            Response.serverError().build()
        }
    }
}
