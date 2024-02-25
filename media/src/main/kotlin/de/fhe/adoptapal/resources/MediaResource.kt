package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.MediaEntity
import de.fhe.adoptapal.model.MediaRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.File
import java.util.*
import javax.print.attribute.standard.Media

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
            val file = File(fileStorePath, mediaEntity?.filePath)

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
            val fileType = file.contentType()

            val file = file.uploadedFile()?.toFile()
            val filePath = File(fileStorePath)

            filePath.mkdirs()

            val newFile = File(filePath, UUID.randomUUID().toString())

            file?.copyTo(newFile)

            mediaRepository.add(file!!.name, fileType)

            Response.status(Response.Status.CREATED).entity(file).build()
        } catch (e: Exception) {
            LOG.error("Failed to create media", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }

    fun deleteFile() {

    }
}