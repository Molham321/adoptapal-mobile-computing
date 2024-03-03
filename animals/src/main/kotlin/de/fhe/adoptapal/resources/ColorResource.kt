package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.ColorEntity
import de.fhe.adoptapal.repository.ColorRepository
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
 * Resource class for handling color-related operations.
 *
 * @property colorRepository The [ColorRepository] for accessing color data.
 */
@RequestScoped
@Path("/animals/colors")
class ColorResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(ColorResource::class.java)
    }

    @Inject
    lateinit var colorRepository: ColorRepository

    /**
     * Retrieves all available colors.
     *
     * @return A [Response] containing the list of [ColorEntity] objects.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Response? {
        val colorEntities: List<ColorEntity> = colorRepository.listAll()

        return if (colorEntities.isNotEmpty()) {
            try {
                LOG.info("get all colors was executed successful")

                Response.ok(colorEntities).build()
            } catch (e: Exception) {
                LOG.error("failed to get all colors", e)
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        } else {
            Response.status(Response.Status.NO_CONTENT).build()
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
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: Long): Response {
        val colorEntity: ColorEntity? = colorRepository.findById(id)

        return if (colorEntity != null) {
            Response.ok(colorEntity).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Color with ID $id not found").build()
        }
    }
}