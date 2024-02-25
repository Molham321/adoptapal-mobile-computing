package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.UserBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import de.fhe.adoptapal.model.*
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

@RequestScoped
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserResource::class.java)
    }

    @Inject
    private lateinit var userBean: UserBean

    @POST
    @Path("/{id}")
    fun create(@PathParam("id") id: Long, request: CreateUserRequest): Response {
        return try {
            userBean.create(id, request.email, request.password, UserEntity.Role.USER)
            return Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to create user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{id}")
    fun get(@PathParam("id") id: Long): Response {
        return try {
            val user = userBean.get(id)
            return Response.ok().entity(UserResponse(user.id!!, user.email)).build()
        } catch (e: Exception) {
            LOG.error("failed to list user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, request: UpdateUserRequest): Response {
        return try {
            userBean.updateAuthorized(RequestSubject(id, request.email, request.password), request.newEmail, request.newPassword, null)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to update user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long, request: DeleteUserRequest): Response {
        return try {
            userBean.deleteAuthorized(RequestSubject(id, request.email, request.password))
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }
}