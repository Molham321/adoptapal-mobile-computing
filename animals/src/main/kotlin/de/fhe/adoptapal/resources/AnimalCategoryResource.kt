package de.fhe.adoptapal.resources

// import com.sun.istack.logging.Logger
import de.fhe.adoptapal.model.AnimalCategoryEntity
import de.fhe.adoptapal.model.AnimalCategoryRepository
import de.fhe.adoptapal.model.ColorEntity
import io.quarkus.arc.ComponentsProvider
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger


@RequestScoped
@Path("/animals/animalCategories")
class AnimalCategoryResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(AnimalCategoryResource::class.java)
    }

    @Inject
    lateinit var animalCategoryRepository: AnimalCategoryRepository

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