package de.fhe.ai.mc.resources

import de.fhe.ai.mc.core.JwtTokenUtils
import de.fhe.ai.mc.core.PasswordUtils
import de.fhe.ai.mc.model.UserEntity
import de.fhe.ai.mc.model.UserRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@RequestScoped
@Path("/")
abstract class AuthResource {

    @Inject
    lateinit var jwt: JsonWebToken

    @Inject
    lateinit var userRepository: UserRepository

    @ConfigProperty(name = "de.fhe.ai.jwt.duration")
    var duration: Long? = null

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    lateinit var issuer: String

    @POST
    @PermitAll
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    fun login( authRequest: AuthRequest): Response {

        val userEntity: UserEntity? = userRepository.find("username", authRequest.username).firstResult()

        return if (userEntity != null && PasswordUtils.verifyPassword(authRequest.password, userEntity.password)) {
            try {
                val newToken = JwtTokenUtils.generateToken(userEntity, duration!!, issuer!!)

                LOG.info( "Created new Token for User ${userEntity.username}" )

                Response.ok(AuthResponse(newToken)).build()
            }
            catch (e: Exception) {
                LOG.warn( "Failed Login Attempt for User ${userEntity.username}" )
                Response.status(Response.Status.UNAUTHORIZED).build()
            }
        }
        else {
            LOG.warn( "Failed Login Attempt for User ${authRequest.username}" )
            Response.status(Response.Status.UNAUTHORIZED).build()
        }
    }

    @GET
    @RolesAllowed("admin")
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    fun admin(): Response? {
        LOG.info( "Admin Access with Token: Username ${jwt.name}, Issuer ${jwt.issuer}, Claims ${jwt.claimNames}" )


        return Response.ok(Message("Content for admin")).build()
    }

    @GET
    @RolesAllowed("user")
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    fun user() = Response.ok(Message("Content for user")).build()

    @GET
    @RolesAllowed("user", "admin")
    @Path("/user-or-admin")
    @Produces(MediaType.APPLICATION_JSON)
    fun userOrAdmin() = Response.ok(Message("Content for user or admin")).build()

    companion object {
        private val LOG: Logger = Logger.getLogger(AuthResource::class.java)
    }

}
