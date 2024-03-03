package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.ColorEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDateTime

/**
 * Repository class for performing CRUD operations on [ColorEntity].
 *
 * This class provides methods for adding and retrieving color entities.
 */
@ApplicationScoped
class ColorRepository : PanacheRepository<ColorEntity> {

    /**
     * Adds a new color entity to the repository.
     *
     * @param name The name of the color.
     * @return The created [ColorEntity].
     */
    @Transactional
    fun add(name: String): ColorEntity {
        val colorEntity = ColorEntity()

        colorEntity.name = name

        colorEntity.createdTimestamp = LocalDateTime.now()
        colorEntity.lastChangeTimestamp = LocalDateTime.now()
        colorEntity.isDeleted = false

        persist(colorEntity)
        return colorEntity
    }
}