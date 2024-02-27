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

@RequestScoped
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserResource::class.java)
    }

    @Inject
    private lateinit var jwt: JsonWebToken

    @Inject
    private lateinit var userBean: UserBean

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