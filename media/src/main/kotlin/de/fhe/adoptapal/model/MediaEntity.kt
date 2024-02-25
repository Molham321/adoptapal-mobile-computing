package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDateTime

enum class mediaType {
    pdf, image, video, unknown
}

// welche Attribute hier notwendig?
// Bilder wie speichern?
// Bilder anhand des Pfades oder der ID finden / im Tier speichern?
// wo werden MediaEntity Einträge genau gespeichert?
// gibt der Service bei findByID nur den Dateipfad zurück oder die Datei?

@Entity
class MediaEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()

    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()

    var isDeleted: Boolean = false

    // var user: Long? = null

    // var animal: Long? = null

    lateinit var filePath: String

    lateinit var mediaType: String
}

@Transactional
@ApplicationScoped
class MediaRepository: PanacheRepository<MediaEntity> {
    // create media/image --> kafka request (aber trotzdem Endpoint)
    // get media/image --> gibt Bild zurück (id als Pfadparameter)
    // delete media/image --> kafka request

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

    // braucht hier keine extra Funktion
    fun findByID(id: Long): MediaEntity? {
        return find("id", id).firstResult();
    }
}