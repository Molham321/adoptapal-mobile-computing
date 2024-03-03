package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

/**
 * Entity class representing a category for animals.
 *
 * @property id The unique identifier for the animal category entity.
 * @property createdTimestamp The timestamp indicating when the animal category entity was initially created.
 * @property name The name of the animal category.
 */
@Entity
class CategoryEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var createdTimestamp: LocalDateTime

    lateinit var name: String
}