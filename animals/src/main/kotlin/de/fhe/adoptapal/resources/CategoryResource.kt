package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.CategoryResponse
import de.fhe.adoptapal.model.CategoryEntity
import de.fhe.adoptapal.repository.CategoryRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

/**
 * Resource class for handling animal category-related operations.
 *
 * This class provides endpoints for retrieving information about animal categories.
 * The base path for these endpoints is "/animals/animalCategories".
 *
 * @property categoryRepository The [CategoryRepository] for accessing animal category data.
 */
@RequestScoped
@Path("/animals/animalCategories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CategoryResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(CategoryResource::class.java)
    }

    @Inject
    lateinit var categoryRepository: CategoryRepository

    private fun entityToResponse(entity: CategoryEntity): CategoryResponse  = CategoryResponse(
        entity.id!!,
        entity.name,
        entity.createdTimestamp
    )

    /**
     * Retrieves all available animal categories.
     *
     * Endpoint: `GET /animals/animalCategories`
     *
     * @return A [Response] containing the list of [CategoryEntity] objects.
     * - If successful, returns a response with HTTP status 200 OK and a list of animal categories.
     * - If no animal categories are found, returns a response with HTTP status 204 No Content.
     * - If an error occurs during the process, returns a response with HTTP status 500 Internal Server Error
     *   and an error message in the response entity.
     */
    @GET
    fun getAll(): Response? {
         return try {
            LOG.info("get all animal categories was executed successful")

            val entities = categoryRepository.findAll().list().map { entityToResponse(it) }
            if (entities.isNotEmpty()) {
                Response.ok(entities).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to get all animal categories", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    /**
     * Retrieves an animal category by its ID.
     *
     * Endpoint: `GET /animals/animalCategories/{id}`
     *
     * @param id The ID of the animal category to retrieve.
     * @return A [Response] containing the [CategoryEntity] with the specified ID.
     * - If the animal category is found, returns a response with HTTP status 200 OK and the corresponding entity.
     * - If no animal category is found, returns a response with HTTP status 404 Not Found and an error message
     *   in the response entity.
     */
    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: Long): Response {
        val entity: CategoryEntity? = categoryRepository.findById(id)

        return if (entity != null) {
            Response.ok(entityToResponse(entity)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Animal category with ID $id not found").build()
        }
    }
}