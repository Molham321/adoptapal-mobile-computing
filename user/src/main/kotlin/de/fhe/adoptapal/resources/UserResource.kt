package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.TokenAuthenticationException
import de.fhe.adoptapal.core.UserBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import de.fhe.adoptapal.model.*
import io.quarkus.security.Authenticated
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger

/**
 * This class represents a RESTful web service resource for managing user-related operations.
 *
 * @property jwt JsonWebToken object for handling JWT authentication.
 * @property userBean UserBean object for interacting with user-related data and business logic.
 */
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

    /**
     * Maps a [UserEntity] object to a [UserResponse] object.
     *
     * @param user The user entity to be mapped.
     * @return A [UserResponse] object representing the user.
     */
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

    /**
     * Endpoint for creating a new user.
     *
     * @param request The [CreateUser] request object containing user details.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST
    fun createUser(request: CreateUser): Response {
        return try {
            Response.ok(userToResponse(userBean.create(request))).build()
        } catch (e: Exception) {
            LOG.error("failed to create user", e)
            mapExceptionToResponse(e)
        }
    }

    /**
     * Endpoint for retrieving user details by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A [Response] containing the user details or an error response.
     */
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

    /**
     * Endpoint for retrieving all users.
     *
     * @return A [Response] containing a list of users or a "No Content" response if no users are found.
     */
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


    /**
     * Endpoint for updating user details by ID.
     *
     * @param id The ID of the user to update.
     * @param rawToken The raw authentication token from the request header.
     * @param request The [UpdateUser] request object containing updated user details.
     * @return A [Response] indicating the success or failure of the operation.
     */
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

    /**
     * Endpoint for deleting a user by ID.
     *
     * @param id The ID of the user to delete.
     * @param credentials The [UserCredentials] object containing user credentials for authentication.
     * @return A [Response] indicating the success or failure of the operation.
     */
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
