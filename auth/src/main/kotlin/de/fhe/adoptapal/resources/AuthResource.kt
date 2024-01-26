package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.JwtBean
import de.fhe.adoptapal.core.PasswordUtils
import de.fhe.adoptapal.model.UserEntity
import de.fhe.adoptapal.model.UserRepository
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.util.Optional

@RequestScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
class AuthResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(AuthResource::class.java)
    }

    @Inject
    private lateinit var jwt: JsonWebToken

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var jwtBean: JwtBean

    private fun validateJwt(): Optional<Response> {
        return try {
            LOG.info("validating token for '${jwt.name}'")
            if (jwtBean.validate(jwt)) {
                Optional.empty()
            } else {
                Optional.of(Response.status(Response.Status.UNAUTHORIZED).build())
            }
        } catch (e: Exception) {
            LOG.error("internal error on validation for token '${jwt.name}'", e)
            Optional.of(Response.serverError().build())
        }
    }

    private fun validatePassword(email: String, password: String): Optional<Response> {
        val userEntity = userRepository.findByEmail(email)

        // user doesn't exist or password doesn't match
        if (userEntity == null || !PasswordUtils.verifyPassword(password, userEntity.password)) {
            LOG.warn("failed login attempt for user '${email}'")
            return Optional.of(Response.status(Response.Status.UNAUTHORIZED).build())
        }

        return Optional.empty()
    }

    @POST
    @Path("/register")
    fun register(request: AuthRequest): Response {
        val existingUser = userRepository.findByEmail(request.email)

        // username is taken
        if (existingUser != null) {
            // TODO(erik): what status and body do we send when a username is taken?
            return Response.status(Response.Status.BAD_REQUEST).build()
        }

        LOG.info("registered user '${request.email}'")
        userRepository.create(request.email, request.password, UserEntity.Role.USER)
        return Response.ok().build()
    }

    @GET
    @Path("/login")
    fun login(request: AuthRequest): Response {
        val errorResponse = validatePassword(request.email, request.password)
        if (errorResponse.isPresent) {
            return errorResponse.get()
        }

        val userEntity = userRepository.findByEmail(request.email)!!
        return try {
            val tokenAndExpiresAt = jwtBean.generateToken(userEntity)
            LOG.info("generated new token for user '${userEntity.email}'")
            Response.ok(LoginResponse(tokenAndExpiresAt.first, tokenAndExpiresAt.second)).build()
        } catch (e: Exception) {
            LOG.error("internal error on login attempt for user '${userEntity.email}'", e)
            Response.serverError().build()
        }
    }

    @POST
    @Path("/delete")
    fun delete(request: AuthRequest): Response {
        val errorResponse = validatePassword(request.email, request.password)
        if (errorResponse.isPresent) {
            return errorResponse.get()
        }

        userRepository.deleteByEmail(request.email)
        return Response.ok().build()
    }

    @PUT
    @Path("/password")
    @Authenticated
    fun password(request: AuthRequest): Response {
        val errorResponse = validateJwt()
        if (errorResponse.isPresent) {
            return errorResponse.get()
        }

        val userEntity = userRepository.findByEmail(jwt.name)

        // user doesn't exist or is trying to access the wrong email
        if (userEntity == null || userEntity.email != jwt.name) {
            LOG.warn("failed password update attempt for user '${request.email}' with auth '${jwt.name}'")
            return Response.status(Response.Status.UNAUTHORIZED).build()
        }

        return try {
            jwtBean.invalidate(jwt)
            userRepository.updatePasswordById(userEntity.id!!, request.password)
            LOG.info("updated password for user '${userEntity.email}'")
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("internal error on login attempt for user '${userEntity.email}'", e)
            Response.serverError().build()
        }
    }

    @GET
    @Path("/validate")
    @Authenticated
    fun validate(): Response {
        return validateJwt().orElse(Response.ok().build())
    }
}
