package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.AnimalEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

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