package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.MediaEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.File
import java.time.LocalDateTime
import java.util.*

/**
 * Repository class for managing media-related database operations.
 *
 * This class extends [PanacheRepository], providing CRUD operations for the [MediaEntity] class.
 * Media files are stored in a file system, and their metadata is managed in the database.
 *
 * @property fileStorePath The path to the directory where media files are stored on the file system.
 */
@Transactional
@ApplicationScoped
class MediaRepository: PanacheRepository<MediaEntity> {

    private var fileStorePath: String = "media-store"

    /**
     * Create a new media entity to the database.
     *
     * @param filePath The path to the stored media file.
     * @param mediaType The media type of the file (e.g., image/jpeg).
     * @return The created [MediaEntity].
     */
    fun create(
        filePath: String,
        mediaType: String
    ): MediaEntity {
        val mediaEntity = MediaEntity()
        mediaEntity.filePath = filePath
        mediaEntity.mediaType = mediaType

        mediaEntity.createdTimestamp = LocalDateTime.now()

        persistAndFlush(mediaEntity)

        return mediaEntity
    }

    /**
     * Uploads a new media file to the file system and creates a corresponding media entity in the database.
     *
     * @param file The [FileUpload] representing the uploaded file.
     * @return The created [MediaEntity].
     */
    @Transactional
    fun uploadMedia(file: FileUpload): MediaEntity {
        val fileType = file.contentType()

        val uploadedFile = file.uploadedFile()?.toFile()
        val filePath = File(fileStorePath)

        filePath.mkdirs()

        val newFile = File(filePath, UUID.randomUUID().toString())

        uploadedFile?.copyTo(newFile)

        val newMediaEntity = create(newFile.name, fileType)

        return newMediaEntity
    }

    /**
     * Retrieves a media entity by its ID from the database.
     *
     * @param id The ID of the media entity.
     * @return The [MediaEntity] if found, otherwise null.
     */
    fun findByID(id: Long): MediaEntity? {
        return find("id", id).firstResult();
    }

    /**
     * Deletes a media entity by its ID from the database.
     *
     * @param id The ID of the media entity to delete.
     */
    fun deleteByID(id: Long) {
        delete("id", id)
    }
}