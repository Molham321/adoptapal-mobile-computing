package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.File
import java.time.LocalDateTime
import java.util.*

@Entity
class MediaEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()

    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()

    var isDeleted: Boolean = false

    lateinit var filePath: String

    lateinit var mediaType: String
}

@Transactional
@ApplicationScoped
class MediaRepository: PanacheRepository<MediaEntity> {
    // create media/image --> kafka request (aber trotzdem Endpoint)
    // get media/image --> gibt Bild zurück (id als Pfadparameter)
    // delete media/image --> kafka request

    private var fileStorePath: String = "media-store"

    fun add(
        filePath: String,
        mediaType: String
    ): MediaEntity {
        val mediaEntity = MediaEntity()
        mediaEntity.filePath = filePath
        mediaEntity.mediaType = mediaType

        mediaEntity.createdTimestamp = LocalDateTime.now()
        mediaEntity.lastChangeTimestamp = LocalDateTime.now()
        mediaEntity.isDeleted = false

        persistAndFlush(mediaEntity)

        return mediaEntity
    }

    @Transactional
    fun uploadMedia(file: FileUpload): MediaEntity {
        val fileType = file.contentType()

        val uploadedFile = file.uploadedFile()?.toFile()
        val filePath = File(fileStorePath)

        filePath.mkdirs()

        val newFile = File(filePath, UUID.randomUUID().toString())

        uploadedFile?.copyTo(newFile)

        val newMediaEntity = add(newFile.name, fileType)

        return newMediaEntity
    }

    fun findByID(id: Long): MediaEntity? {
        return find("id", id).firstResult();
    }

    fun deleteByID(id: Long) {
        delete("id", id)
    }
}