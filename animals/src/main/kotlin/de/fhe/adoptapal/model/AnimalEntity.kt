package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Entity class representing an animal available for adoption.
 *
 * @property id The unique identifier for the animal entity.
 * @property createdTimestamp The timestamp indicating when the animal entity was initially created.
 * @property owner The ID of the owner of the animal.
 * @property name The name of the animal.
 * @property description The description or details about the animal.
 * @property color The ID of the color associated with the animal.
 * @property isMale A flag indicating whether the animal is male.
 * @property animalCategory The ID of the animal category to which the animal belongs.
 * @property birthday The birthday or birthdate of the animal.
 * @property weight The weight of the animal.
 * @property image The ID of the image associated with the animal.
 */
@Entity
class AnimalEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var createdTimestamp: LocalDateTime

    var owner: Long = 0

    lateinit var name: String

    lateinit var description: String

    var color: Long = 0

    var isMale: Boolean = false

    var animalCategory: Long = 0

    lateinit var birthday: LocalDate

    var weight: Float = 0.0f

    var image: Long = 0
}