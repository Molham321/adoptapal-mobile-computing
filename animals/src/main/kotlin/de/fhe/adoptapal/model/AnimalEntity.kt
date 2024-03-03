package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Entity class representing an animal available for adoption.
 *
 * @property id The unique identifier for the animal entity.
 * @property createdTimestamp The timestamp indicating when the animal entity was initially created.
 * @property lastChangeTimestamp The timestamp indicating the last time the animal entity was modified.
 * @property isDeleted A flag indicating whether the animal entity is marked as deleted.
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

    var createdTimestamp: LocalDateTime = LocalDateTime.now()

    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()

    var isDeleted: Boolean = false

    var owner: Long? = null

    lateinit var name: String

    lateinit var description: String

    var color: Long? = null

    var isMale: Boolean = false

    var animalCategory: Long? = null

    lateinit var birthday: LocalDate

    var weight: Float = 0.0f

    var image: Long? = null
}

/**
 * Repository class for performing CRUD operations on [AnimalEntity].
 *
 * This class provides methods for adding, retrieving, updating, and deleting animal entities.
 */
@Transactional
@ApplicationScoped
class AnimalRepository : PanacheRepository<AnimalEntity> {

    /**
     * Adds a new animal entity to the repository.
     *
     * @param name The name of the animal.
     * @param description The description or details about the animal.
     * @param color The ID of the color associated with the animal.
     * @param isMale A flag indicating whether the animal is male.
     * @param animalCategory The ID of the animal category to which the animal belongs.
     * @param birthday The birthday or birthdate of the animal.
     * @param weight The weight of the animal.
     * @param owner The ID of the owner of the animal.
     * @param image The ID of the image associated with the animal.
     */
    fun add(
        name: String,
        description: String,
        color: Long?,
        isMale: Boolean,
        animalCategory: Long?,
        birthday: LocalDate,
        weight: Float,
        owner: Long?,
        image: Long?
    ) {
        val animalEntity = AnimalEntity()
        animalEntity.name = name
        animalEntity.description = description
        animalEntity.color = color
        animalEntity.isMale = isMale
        animalEntity.animalCategory = animalCategory
        animalEntity.birthday = birthday
        animalEntity.weight = weight
        animalEntity.owner = owner
        animalEntity.image = image

        animalEntity.createdTimestamp = LocalDateTime.now()
        animalEntity.lastChangeTimestamp = LocalDateTime.now()
        animalEntity.isDeleted = false

        persist(animalEntity)
    }

    /**
     * Retrieves a list of animals owned by a specific user.
     *
     * @param owner The ID of the owner.
     * @return The list of animals owned by the specified user.
     */
    fun getAnimalsByOwner(owner: Long): List<AnimalEntity> {
        return find("owner", owner).list();
    }

    @Transactional
    fun createAnimal(animalEntity: AnimalEntity) {
        persist(animalEntity);
    }

    @Transactional
    fun updateAnimal(animalEntity: AnimalEntity) {
        // updateAnimal notwendig?
    }

   @Transactional
    fun deleteAnimal(id: Long) {
        delete("id", id);
    }
}