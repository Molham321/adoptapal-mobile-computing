package de.fhe.adoptapal.resources

import de.fhe.adoptapal.messaging.KafkaProducer
import de.fhe.adoptapal.model.UserEntity
import de.fhe.adoptapal.repository.UserRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.xml.bind.ValidationException
import org.jboss.logging.Logger

@RequestScoped
@Path("/users")
class UserResource {

    companion object {
        private val LOG: Logger = Logger.getLogger(UserResource::class.java)
    }

    @Inject
    lateinit var userRepository: UserRepository

    // kafka test
    @Inject
    lateinit var producer: KafkaProducer

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Response {
        return try {
            val users: List<UserEntity> = userRepository.listAll()
            if (users.isNotEmpty()) {
                LOG.info("Successfully retrieved all users")
                Response.ok(users).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("Failed to get all users", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: Long): Response {
        return userRepository.findById(id)?.let {
            Response.ok(it).build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("User with ID $id not found").build()
    }

    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getByEmail(@PathParam("email") email: String): Response {
        return userRepository.findByEmail(email)?.let {
            Response.ok(it).build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("User with email $email not found").build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(newUser: UserEntity): Response {
        return try {
            userRepository.validateUser(newUser)
            userRepository.findByEmail(newUser.email!!)?.let {
                throw ValidationException("Email is already in use")
            }
            userRepository.createUser(newUser)
            LOG.info("User created successfully")
            Response.status(Response.Status.CREATED).entity(newUser).build()
        } catch (e: ValidationException) {
            LOG.error("Validation error during user creation", e)
            Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
        } catch (e: Exception) {
            LOG.error("Failed to create user", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateById(@PathParam("id") id: Long, updatedUser: UserEntity): Response {
        return userRepository.findById(id)?.let { existingUser ->
            try {
                userRepository.updateExistingUser(existingUser, updatedUser)
                userRepository.updateUser(existingUser)
                Response.ok(existingUser).build()
            } catch (e: ValidationException) {
                LOG.error("Validation error during user update", e)
                Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
            } catch (e: Exception) {
                LOG.error("Failed to update user", e)
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        } ?: Response.status(Response.Status.NOT_FOUND).entity("User with ID $id not found").build()
    }

    @DELETE
    @Path("/{id}")
    fun deleteById(@PathParam("id") id: Long): Response {
        return userRepository.findById(id)?.let { userEntity ->

            // Sende die Benutzer-ID an den Kafka-Kanal für Benutzerlöschung
            producer.sendPost(id);

            userRepository.deleteUser(id)
            Response.ok(userEntity).build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("User with ID $id not found").build()
    }
}