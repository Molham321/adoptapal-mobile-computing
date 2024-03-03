package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.ColorResponse
import de.fhe.adoptapal.model.ColorEntity
import de.fhe.adoptapal.model.ErrorResponse
import de.fhe.adoptapal.repository.ColorRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger


/**
 * Resource class for handling color-related operations.
 *
 * @property colorRepository The [ColorRepository] for accessing color data.
 */
@RequestScoped
@Path("/animals/colors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ColorResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(ColorResource::class.java)
    }

    @Inject
    lateinit var colorRepository: ColorRepository

    private fun entityToResponse(entity: ColorEntity): ColorResponse = ColorResponse(
        entity.id!!,
        entity.name,
        entity.createdTimestamp,
    )

    /**
     * Retrieves all available colors.
     *
     * @return A [Response] containing the list of [ColorEntity] objects.
     */
    @GET
    fun getAll(): Response? {
         return try {
            LOG.info("get all colors was executed successful")

            val entities = colorRepository.findAll().list().map { entityToResponse(it) }
            if (entities.isNotEmpty()) {
                Response.ok(entities).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to get all colors", e)
            Response.serverError().build()
        }
    }

    /**
     * Retrieves a color by its ID.
     *
     * @param id The ID of the color to retrieve.
     * @return A [Response] containing the [ColorEntity] with the specified ID.
     */
    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: Long): Response {
        val entity: ColorEntity? = colorRepository.findById(id)

        return if (entity != null) {
            Response.ok(entityToResponse(entity)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity(ErrorResponse("Color with ID $id not found")).build()
        }
    }
}