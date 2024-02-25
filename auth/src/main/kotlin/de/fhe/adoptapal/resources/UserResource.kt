package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.PasswordUtils
import de.fhe.adoptapal.model.TokenRepository
import de.fhe.adoptapal.model.UserEntity
import de.fhe.adoptapal.model.UserRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

@RequestScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserResource::class.java)
    }

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var tokenRepository: TokenRepository

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

    @POST
    @Path("/{id}")
    fun createUser(@PathParam("id") id: Long, request: CreateUserRequest): Response {
        LOG.info("creating user with id `$id`")
        return try {
            val user = userRepository.create(id, request.email, request.password, UserEntity.Role.USER)
            return Response.ok(UserResponse(user.id!!, user.email)).build()
        } catch (e: Exception) {
            // TODO: handle unique clash differently
            LOG.error("failed to create user with id `$id`", e)
            Response.serverError().build()
        }
    }

    @GET
    @Path("/{id}")
    fun getUser(@PathParam("id") id: Long): Response {
        LOG.info("listing user with id `$id`")
        val user = userRepository.findById(id)
        return try {
            if (user != null) {
                Response.ok(UserResponse(user.id!!, user.email)).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).entity("User with id `$id` not found").build()
            }
        } catch (e: Exception) {
            LOG.error("failed to list user with id `$id`", e)
            Response.serverError().build()
        }
    }

    @PUT
    @Path("/{id}")
    fun updateUser(@PathParam("id") id: Long, request: UpdateUserRequest): Response {
        LOG.info("updating user with id `$id`")
        return try {
            val e = validatePassword(id, request.password)
            if (e != null) {
                return e
            }

            if (request.newPassword == null && request.newEmail == null || request.newPassword != null && request.newEmail != null) {
                LOG.error("updated request for user with id `$id` contained zero or more than one update")
                return Response.status(Response.Status.BAD_REQUEST).entity("Request must have exactly one update").build()
            }

            // invalidate all previous tokens of this user
            tokenRepository.deleteAllForUser(id)

            if (request.newPassword != null) {
                LOG.info("updated password for user with id `$id`")
                userRepository.updatePassword(id, request.newPassword!!)
            }

            if (request.newEmail != null) {
                LOG.info("updated email for user with id `$id`")
                userRepository.updateEmail(id, request.newEmail!!)
            }

            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to update user with id `$id`", e)
            Response.serverError().build()
        }
    }

    @DELETE
    @Path("/{id}")
    fun deleteUser(@PathParam("id") id: Long, request: DeleteUserRequest): Response {
        LOG.info("deleting user with id `$id`")
        return try {
            val e = validatePassword(id, request.password)
            if (e != null) {
                return e
            }

            userRepository.delete(id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete user with id `$id`", e)
            Response.serverError().build()
        }
    }
}