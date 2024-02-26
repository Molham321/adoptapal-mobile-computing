package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.UserBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import de.fhe.adoptapal.model.CreateUser
import de.fhe.adoptapal.model.UpdateUser
import de.fhe.adoptapal.model.UserCredentials
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
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
    lateinit var userBean: UserBean

    @POST
    fun createUser(request: CreateUser): Response {
        return try {
            Response.ok(userBean.create(request)).build()
        } catch (e: Exception) {
            LOG.error("failed to create user", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{id}")
    fun get(@PathParam("id") id: Long): Response {
        return try {
            Response.ok(userBean.get(id)).build()
        } catch (e: Exception) {
            LOG.error("failed to get user with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/email/{email}")
    fun get(@PathParam("email") email: String): Response {
        return try {
            Response.ok(userBean.get(email)).build()
        } catch (e: Exception) {
            LOG.error("failed to get user with email `$email`", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    fun getAll(): Response {
        return try {
            val users = userBean.getAll()
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
}
