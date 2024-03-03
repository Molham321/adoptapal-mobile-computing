package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

/**
 * Entity class representing media-related data stored in the database.
 *
 * This class is annotated with [@Entity] to mark it as a JPA entity, and it is used
 * to map media-related information to the database. Instances of this class are managed
 * by the associated [MediaRepository].
 */
@Entity
class MediaEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var createdTimestamp: LocalDateTime
    lateinit var filePath: String
    lateinit var mediaType: String
}