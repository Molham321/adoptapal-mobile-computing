package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.CategoryEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDateTime

/**
 * Repository class for performing CRUD operations on [CategoryEntity].
 *
 * This class provides methods for adding, retrieving, updating, and deleting animal category entities.
 */
@ApplicationScoped
class CategoryRepository : PanacheRepository<CategoryEntity> {

    /**
     * Adds a new animal category entity to the repository.
     *
     * @param name The name of the animal category.
     * @return The newly created animal category entity.
     */
    @Transactional
    fun create(name: String): CategoryEntity {
        val animalCategoryEntity = CategoryEntity()

        animalCategoryEntity.name = name
        animalCategoryEntity.createdTimestamp = LocalDateTime.now()

        persist(animalCategoryEntity)
        return animalCategoryEntity
    }
}