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
 * @property name The name of the color.
 */
@Entity
class ColorEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var createdTimestamp: LocalDateTime

    lateinit var name: String
}