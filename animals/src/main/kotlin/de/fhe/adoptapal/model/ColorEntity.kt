package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

/**
 * Entity class representing a color used for categorizing animals.
 *
 * @property id The unique identifier for the color entity.
 * @property createdTimestamp The timestamp indicating when the color entity was initially created.
 * @property lastChangeTimestamp The timestamp indicating the last time the color entity was modified.
 * @property isDeleted A flag indicating whether the color entity is marked as deleted.
 * @property name The name of the color.
 */
@Entity
class ColorEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()

    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()

    var isDeleted: Boolean = false

    lateinit var name: String
}