package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.MediaEntity
import de.fhe.adoptapal.repository.MediaRepository
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

/**
 * Resource class for handling media-related operations.
 *
 * This class provides endpoints for retrieving, uploading, and deleting media files.
 * Media files are stored in a designated file store path, and their metadata is managed
 * by the associated [MediaRepository].
 *
 * @property mediaRepository Injected instance of [MediaRepository] for accessing media-related data.
 * @property fileStorePath The path to the directory where media files are stored.
 */
@RequestScoped
@Path("/media")
class MediaResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(MediaResource::class.java)
    }

    @Inject
    lateinit var mediaRepository: MediaRepository

    private var fileStorePath: String = "media-store"

    /**
     * Retrieves a media file by its ID.
     *
     * @param id The ID of the media file.
     * @return A [Response] containing the media file as an octet stream if found,
     *         or a 404 Not Found response if the media file does not exist.
     */
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

    /**
     * Uploads a new media file.
     *
     * @param file The [FileUpload] representing the uploaded file.
     * @return A [Response] containing the newly created media entity as JSON if successful,
     *         or a 500 Internal Server Error response if the upload fails.
     */
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

    /**
     * Deletes a media file by its ID.
     *
     * @param id The ID of the media file to delete.
     * @return A [Response] indicating success if the media file is deleted,
     *         or a 404 Not Found response if the media file does not exist.
     */
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