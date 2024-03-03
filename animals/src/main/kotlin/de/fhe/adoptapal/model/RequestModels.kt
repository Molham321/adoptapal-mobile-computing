package de.fhe.adoptapal.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Data class representing the request body for creation of an animal.
 *
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
class CreateAnimal {
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

/**
 * Data class representing the response body for an animal response.
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
data class AnimalResponse(
    val id: Long,
    val owner: Long,
    val name: String,
    val description: String,
    val color: Long,
    val isMale: Boolean,
    val animalCategory: Long,
    val birthday: LocalDate,
    val weight: Float,
    val image: Long,
    var createdTimestamp: LocalDateTime,
)

/**
 * Data class representing the response body for a category response.
 *
 * @property id The unique identifier for the category entity.
 * @property createdTimestamp The timestamp indicating when the category entity was initially created.
 * @property name The name of the category.
 */
data class CategoryResponse(
    val id: Long,
    val name: String,
    var createdTimestamp: LocalDateTime,
)

/**
 * Data class representing the response body for an color response.
 *
 * @property id The unique identifier for the color entity.
 * @property createdTimestamp The timestamp indicating when the color entity was initially created.
 * @property name The name of the color.
 */
data class ColorResponse(
    val id: Long,
    val name: String,
    var createdTimestamp: LocalDateTime,
)

/**
 * Data class representing the response body for an error.
 *
 * @property reason The reason for the error.
 */
data class ErrorResponse(val reason: String)