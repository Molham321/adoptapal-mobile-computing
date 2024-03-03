package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
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

/**
 * Repository class for performing CRUD operations on [AnimalCategoryEntity].
 *
 * This class provides methods for adding, retrieving, updating, and deleting animal category entities.
 */
@ApplicationScoped
class AnimalCategoryRepository : PanacheRepository<AnimalCategoryEntity> {

    /**
     * Adds a new animal category entity to the repository.
     *
     * @param name The name of the animal category.
     * @return The newly created animal category entity.
     */
    @Transactional
    fun add(name: String): AnimalCategoryEntity {
        val animalCategoryEntity = AnimalCategoryEntity()

        animalCategoryEntity.name = name

        animalCategoryEntity.createdTimestamp = LocalDateTime.now()
        animalCategoryEntity.lastChangeTimestamp = LocalDateTime.now()
        animalCategoryEntity.isDeleted = false

        persist(animalCategoryEntity)
        return animalCategoryEntity
    }
}