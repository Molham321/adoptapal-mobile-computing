package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.AnimalEntity
import de.fhe.adoptapal.model.AnimalRepository
import io.quarkus.arc.ComponentsProvider.LOG
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

@RequestScoped
@Path("/animals")
class AnimalResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(ColorResource::class.java)
    }

    @Inject
    lateinit var animalRepository: AnimalRepository

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createAnimal(newAnimal: AnimalEntity): Response {
        return try {
            animalRepository.persist(newAnimal)
            LOG.info("Animal created successfully")
            Response.status(Response.Status.CREATED).entity(newAnimal).build()
        } catch (e: Exception) {
            LOG.error("Failed to create animal", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    @DELETE
    @Path("/{id}")
    fun deleteAnimal(@PathParam("id") id: Long): Response {
        return animalRepository.findById(id)?.let { animalEntity ->
            animalRepository.deleteById(id)
            Response.ok().entity("Animal with ID $id deleted").build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("Animal with ID $id not found").build()
    }
}