package de.fhe.adoptapal.resources

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
 * Resource class for handling user-related operations.
 */
@RequestScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserResource::class.java)
    }

    @Inject
    private lateinit var jwt: JsonWebToken

    @Inject
    private lateinit var userBean: UserBean

    /**
     * Endpoint for creating a new user.
     *
     * @param request The request body containing user information.
     * @return Response indicating the success or failure of the operation.
     */
    @POST
    fun create(request: CreateUser): Response {
        return try {
            val user = userBean.create(request)
            return Response.ok(UserResponse(user.id!!, user.email)).build()
        } catch (e: Exception) {
            LOG.error("failed to create user", e)
            mapExceptionToResponse(e)
        }
    }

    /**
     * Endpoint for retrieving user information by user ID.
     *
     * @param id The ID of the user.
     * @return Response containing user information if successful, or an error response.
     */
    @GET
    @Path("/{id}")
    @Authenticated
    fun get(@PathParam("id") id: Long): Response {
        return try {
            userBean.validateCredentials(jwt, id)
            val user = userBean.get(id)
            return Response.ok().entity(UserResponse(user.id!!, user.email)).build()
        } catch (e: Exception) {
            LOG.error("failed to list user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    /**
     * Endpoint for updating user information by user ID.
     *
     * @param id The ID of the user.
     * @param credentials User credentials for authentication.
     * @param request The request body containing updated user information.
     * @return Response indicating the success or failure of the operation.
     */
    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, @BeanParam credentials: UserCredentials, request: UpdateUser): Response {
        return try {
            userBean.validateCredentials(credentials, id)
            userBean.update(id, request)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to update user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    /**
     * Endpoint for deleting a user by user ID.
     *
     * @param id The ID of the user.
     * @param credentials User credentials for authentication.
     * @return Response indicating the success or failure of the operation.
     */
    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long, @BeanParam credentials: UserCredentials): Response {
        return try {
            userBean.validateCredentials(credentials, id)
            userBean.delete(id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    /**
     * Endpoint for validating user credentials.
     *
     * @param userId The ID of the user to validate.
     * @param credentials User credentials for validation.
     * @return Response indicating the success or failure of the validation.
     */
    @GET
    @Path("/{userId}/validate")
    fun validate(@PathParam("userId") userId: Long, @BeanParam credentials: UserCredentials): Response {
        return try {
            userBean.validateCredentials(credentials, userId)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed validation of password for user with id `$userId`", e)
            mapExceptionToResponse(e)
        }
    }
}