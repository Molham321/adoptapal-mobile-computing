package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.AnimalCategoryEntity
import de.fhe.adoptapal.model.AnimalCategoryRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

/**
 * Resource class for handling animal category-related operations.
 *
 * This class provides endpoints for retrieving information about animal categories.
 * The base path for these endpoints is "/animals/animalCategories".
 *
 * @property animalCategoryRepository The [AnimalCategoryRepository] for accessing animal category data.
 */
@RequestScoped
@Path("/animals/animalCategories")
class AnimalCategoryResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(AnimalCategoryResource::class.java)
    }

    @Inject
    lateinit var animalCategoryRepository: AnimalCategoryRepository

    /**
     * Retrieves all available animal categories.
     *
     * Endpoint: `GET /animals/animalCategories`
     *
     * @return A [Response] containing the list of [AnimalCategoryEntity] objects.
     * - If successful, returns a response with HTTP status 200 OK and a list of animal categories.
     * - If no animal categories are found, returns a response with HTTP status 204 No Content.
     * - If an error occurs during the process, returns a response with HTTP status 500 Internal Server Error
     *   and an error message in the response entity.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Response? {
        val animalCategoryEntities: List<AnimalCategoryEntity> = animalCategoryRepository.listAll()

        return if (animalCategoryEntities.isNotEmpty()) {
            try {
                LOG.info("get all animal categories was executed successful")

                Response.ok(animalCategoryEntities).build()
            } catch (e: Exception) {

                LOG.error("failed to get all animal categories", e)
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        } else {
            Response.status(Response.Status.NO_CONTENT).build()
        }
    }

    /**
     * Retrieves an animal category by its ID.
     *
     * Endpoint: `GET /animals/animalCategories/{id}`
     *
     * @param id The ID of the animal category to retrieve.
     * @return A [Response] containing the [AnimalCategoryEntity] with the specified ID.
     * - If the animal category is found, returns a response with HTTP status 200 OK and the corresponding entity.
     * - If no animal category is found, returns a response with HTTP status 404 Not Found and an error message
     *   in the response entity.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: Long): Response {
        val animalCategoryEntity: AnimalCategoryEntity? = animalCategoryRepository.findById(id)

        return if (animalCategoryEntity != null) {
            Response.ok(animalCategoryEntity).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Animal category with ID $id not found").build()
        }
    }
}