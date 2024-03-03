package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDateTime

/**
 * Entity class representing a color used for categorizing animals.
 *
 * @property id The unique identifier for the color entity.
 * @property createdTimestamp The timestamp indicating when the color entity was initially created.
 * @property lastChangeTimestamp The timestamp indicating the last time the color entity was modified.
 * @property isDeleted A flag indicating whether the color entity is marked as deleted.
 * @property name The name of the color.
 */
@Entity
class ColorEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()

    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()

    var isDeleted: Boolean = false

    lateinit var name: String
}

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