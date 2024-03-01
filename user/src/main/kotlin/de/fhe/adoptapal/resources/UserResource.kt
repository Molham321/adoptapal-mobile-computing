package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.TokenAuthenticationException
import de.fhe.adoptapal.core.UserBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import de.fhe.adoptapal.model.*
import io.quarkus.security.Authenticated
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger

@RequestScoped
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserResource::class.java)
    }

    @Inject
    private lateinit var jwt: JsonWebToken

    @Inject
    lateinit var userBean: UserBean

    fun userToResponse(user: UserEntity) = UserResponse(
        user.id!!,
        user.username,
        user.phoneNumber,
        user.authId!!,
        AddressResponse(
            user.address.street,
            user.address.city,
            user.address.postalCode,
        ),
    )

    @POST
    fun createUser(request: CreateUser): Response {
        return try {
            Response.ok(userToResponse(userBean.create(request))).build()
        } catch (e: Exception) {
            LOG.error("failed to create user", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{id}")
    fun get(@PathParam("id") id: Long): Response {
        return try {
            Response.ok(userToResponse(userBean.get(id))).build()
        } catch (e: Exception) {
            LOG.error("failed to get user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    fun getAll(): Response {
        return try {
            val users = userBean.getAll().map { userToResponse(it) }
            if (users.isNotEmpty()) {
                Response.ok(users).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to get all users", e)
            mapExceptionToResponse(e)
        }
    }

    @PUT
    @Path("/{id}")
    @Authenticated
    fun update(@PathParam("id") id: Long, @HeaderParam("Authorization") rawToken: String, request: UpdateUser): Response {
        return try {
            val token = if (rawToken.startsWith("Bearer ")) {
                rawToken.substringAfter(" ")
            } else {
                throw TokenAuthenticationException()
            }

            userBean.validateCredentials(token, id)
            userBean.update(id, request)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to update user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long, @BeanParam credentials: UserCredentials): Response {
        return try {
            userBean.validateCredentials(credentials, id)
            userBean.delete(credentials, id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }
}
