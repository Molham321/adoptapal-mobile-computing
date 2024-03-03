package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.AuthServiceClient
import de.fhe.adoptapal.model.*
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
import java.time.LocalDate

/**
 * Resource class for handling animal-related operations.
 *
 * @property animalRepository The [AnimalRepository] for accessing animal data.
 * @property jwt The JSON Web Token for authentication.
 * @property authService The [AuthServiceClient] for communication with the authentication service.
 */
@RequestScoped
@Path("/animals")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class AnimalResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(AnimalResource::class.java)
    }

    @Inject
    lateinit var animalRepository: AnimalRepository

    private fun entityToResponse(entity: AnimalEntity): AnimalResponse = AnimalResponse(
            entity.id!!,
            entity.owner,
            entity.name,
            entity.description,
            entity.color,
            entity.isMale,
            entity.animalCategory,
            entity.birthday,
            entity.weight,
            entity.image,
            entity.createdTimestamp,
    )

    @Inject
    private lateinit var jwt: JsonWebToken

    private var authService: AuthServiceClient = QuarkusRestClientBuilder.newBuilder()
            .baseUri(URI.create("http://auth:8080/"))
            .build(AuthServiceClient::class.java)

    /**
     * Retrieves all available animals.
     *
     * @return A [Response] containing the list of [AnimalEntity] objects.
     */
    @GET
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
    fun getByOwner(@PathParam("owner") owner: Long): Response {
        return try {
            LOG.info("get animals by owner was executed successful")

            val entities = animalRepository.getAnimalsByOwner(owner).map { entityToResponse(it) }
            if (entities.isNotEmpty()) {
                Response.ok(entities).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to get animals by owner", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    /**
     * Creates a new animal.
     *
     * @param request The [CreateAnimal] representing the new animal.
     * @param rawToken The raw authentication token.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST
    @Authenticated
    fun createAnimal(request: CreateAnimal, @HeaderParam("Authorization") rawToken: String): Response {
        return try {
            if (rawToken.startsWith("Bearer ")) {
                try {
                    authService.isTokenValid(request.owner, rawToken.substringAfter(" "))
                } catch (e: Exception) {
                    return Response.status(Response.Status.UNAUTHORIZED).build()
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build()
            }

            val entity = animalRepository.create(
                request.name,
                request.description,
                request.color,
                request.isMale,
                request.animalCategory,
                request.birthday,
                request.weight,
                request.owner,
                request.image,
            )
            LOG.info("Animal created successfully")
            Response.status(Response.Status.CREATED).entity(entityToResponse(entity)).build()
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