package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.AnimalCategoryEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDateTime

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