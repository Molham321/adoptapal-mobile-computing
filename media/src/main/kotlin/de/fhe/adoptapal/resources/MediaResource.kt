package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.MediaEntity
import de.fhe.adoptapal.model.MediaRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.File

@RequestScoped
@Path("/media")
class MediaResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(MediaResource::class.java)
    }

    @Inject
    lateinit var mediaRepository: MediaRepository

    private var fileStorePath: String = "media-store"

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun getById(@PathParam("id") id: Long): Response {
        val mediaEntity: MediaEntity? = mediaRepository.findByID(id)

        return if(mediaEntity != null) {
            val file = File(fileStorePath, mediaEntity.filePath)

            LOG.info(file)

            LOG.info("File ${file.name} found")
            Response.ok(file).header(HttpHeaders.CONTENT_TYPE, mediaEntity.mediaType).build();
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Media with id $id not found").build()
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    fun uploadFile(@RestForm file: FileUpload): Response {
        return try {
            val newMediaEntity = mediaRepository.uploadMedia(file)

            LOG.info("Media with ID ${newMediaEntity.id} created successfully")
            Response.status(Response.Status.CREATED).entity(newMediaEntity).build()
        } catch (e: Exception) {
            LOG.error("Failed to create media", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    @DELETE
    @Path("/delete/{id}")
    fun deleteFile(@PathParam("id") id: Long): Response {
        val mediaToDelete: MediaEntity? = mediaRepository.findByID(id)

        return if(mediaToDelete != null) {
            val fileToDelete = File(fileStorePath, mediaToDelete.filePath)

            fileToDelete.delete()

            mediaRepository.deleteByID(id)

            LOG.info("Media with ID ${mediaToDelete.id} deleted successfully")
            Response.ok().entity("Media with ID $id deleted").build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Media with id $id not found").build()
        }
    }
}