package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.AuthServiceClient
import de.fhe.adoptapal.model.AnimalEntity
import de.fhe.adoptapal.repository.AnimalRepository
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder
import io.quarkus.security.Authenticated
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import java.net.URI

/**
 * Resource class for handling animal-related operations.
 *
 * @property animalRepository The [AnimalRepository] for accessing animal data.
 * @property jwt The JSON Web Token for authentication.
 * @property authService The [AuthServiceClient] for communication with the authentication service.
 */
@RequestScoped
@Path("/animals")
class AnimalResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(AnimalResource::class.java)
    }

    @Inject
    lateinit var animalRepository: AnimalRepository

    @Inject
    private lateinit var jwt: JsonWebToken

    private var authService: AuthServiceClient =
        QuarkusRestClientBuilder.newBuilder().baseUri(URI.create("http://auth:8080/"))
            .build(AuthServiceClient::class.java)

    /**
     * Retrieves all available animals.
     *
     * @return A [Response] containing the list of [AnimalEntity] objects.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Response? {
        val animalEntities: List<AnimalEntity> = animalRepository.listAll()

        return if (animalEntities.isNotEmpty()) {
            try {
                LOG.info("get all animals was executed successful")

                Response.ok(animalEntities).build()
            } catch (e: Exception) {
                LOG.error("failed to get all animals", e)
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        } else {
            Response.status(Response.Status.NO_CONTENT).build()
        }
    }

    /**
     * Retrieves an animal by its ID.
     *
     * @param id The ID of the animal to retrieve.
     * @return A [Response] containing the [AnimalEntity] with the specified ID.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: Long): Response {
        val animalEntity: AnimalEntity? = animalRepository.findById(id)

        return if (animalEntity != null) {
            Response.ok(animalEntity).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Animal with ID $id not found").build()
        }
    }

    /**
     * Retrieves animals by owner ID.
     *
     * @param owner The ID of the owner.
     * @return A [Response] containing the list of [AnimalEntity] objects owned by the specified owner.
     */
    @GET
    @Path("/owner/{owner}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getByOwner(@PathParam("owner") owner: Long): Response {
        val animalEntities: List<AnimalEntity> = animalRepository.getAnimalsByOwner(owner)

        return if (animalEntities.isNotEmpty()) {
            try {
                LOG.info("get animals by owner was executed successful")

                Response.ok(animalEntities).build()
            } catch (e: Exception) {
                LOG.error("failed to get animals by owner", e)
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        } else {
            Response.status(Response.Status.NO_CONTENT).build()
        }
    }

    /**
     * Creates a new animal.
     *
     * @param newAnimal The [AnimalEntity] representing the new animal.
     * @param rawToken The raw authentication token.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    fun createAnimal(newAnimal: AnimalEntity, @HeaderParam("Authorization") rawToken: String): Response {
        return try {
            if (rawToken.startsWith("Bearer ")) {
                try {
                    authService.isTokenValid(newAnimal.owner!!, rawToken.substringAfter(" "))
                } catch (e: Exception) {
                    return Response.status(Response.Status.UNAUTHORIZED).build()
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build()
            }

            animalRepository.persist(newAnimal)
            LOG.info("Animal created successfully")
            Response.status(Response.Status.CREATED).entity(newAnimal).build()
        } catch (e: Exception) {
            LOG.error("Failed to create animal", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    /**
     * Deletes an animal by its ID.
     *
     * @param id The ID of the animal to delete.
     * @param rawToken The raw authentication token.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @DELETE
    @Path("/{id}")
    @Authenticated
    fun deleteAnimal(@PathParam("id") id: Long, @HeaderParam("Authorization") rawToken: String): Response {
        return animalRepository.findById(id)?.let { animalEntity ->

            if (rawToken.startsWith("Bearer ")) {
                try {
                    authService.isTokenValid(animalEntity.owner!!, rawToken.substringAfter(" "))
                } catch (e: Exception) {
                    return Response.status(Response.Status.UNAUTHORIZED).build()
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build()
            }

            animalRepository.deleteById(id)
            Response.ok().entity("Animal with ID $id deleted").build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("Animal with ID $id not found").build()
    }
}