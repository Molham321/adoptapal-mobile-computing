package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.JwtTokenUtils
import de.fhe.adoptapal.core.PasswordUtils
import de.fhe.adoptapal.model.UserRepository
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

// TODO(erik): use reactive API
// BUG(erik): unsupported content type on login

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
    @ConfigProperty(name = "de.fhe.ai.jwt.duration")
    private var duration: Long? = null

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private lateinit var issuer: String

    @POST
    @Path("/register")
    fun register(request: AuthRequest): Response {
        val existingUser = userRepository.findByUsername(request.username)

        // username is taken
        if (existingUser != null) {
            // TODO(erik): what status and body do we send when a username is taken?
            return Response.status(Response.Status.BAD_REQUEST).build()
        }

        LOG.info("registered user '${request.username}'")
        userRepository.add(request.username, request.password)
        return Response.ok().build()
    }

    @GET
    @Path("/login")
    fun login(request: AuthRequest): Response {
        val userEntity = userRepository.findByUsername(request.username)

        // user doesn't exits or password doesn't match
        if (userEntity == null || !PasswordUtils.verifyPassword(request.password, userEntity.password)) {
            LOG.warn("failed login attempt for user '${request.username}'")
            return Response.status(Response.Status.UNAUTHORIZED).build()
        }

        return try {
            val token = JwtTokenUtils.generateToken(userEntity, duration!!, issuer)
            LOG.info("generated new token for user '${userEntity.username}'")
            Response.ok(LoginResponse(token, duration!!)).build()
        } catch (e: Exception) {
            LOG.error("internal error on login attempt for user '${userEntity.username}'", e)
            Response.serverError().build()
        }
    }

    @GET
    @Authenticated
    @Path("/validate")
    fun validate(): Response {
        LOG.info("Validated token for '${jwt.name}'")
        return Response.ok().build()
    }
}
