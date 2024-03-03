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
 * @property lastChangeTimestamp The timestamp indicating the last time the animal category entity was modified.
 * @property isDeleted A flag indicating whether the animal category entity is marked as deleted.
 * @property name The name of the animal category.
 */
@Entity
class AnimalCategoryEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()

    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()

    var isDeleted: Boolean = false

    lateinit var name: String
}