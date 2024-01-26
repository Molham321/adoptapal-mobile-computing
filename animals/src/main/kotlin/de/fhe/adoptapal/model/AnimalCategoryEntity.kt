package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDateTime

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

@ApplicationScoped
class AnimalCategoryRepository: PanacheRepository<AnimalCategoryEntity> {
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

//    fun getAllAnimalCategories(): List<AnimalCategoryEntity> {
//        return listAll();
//    }
//
//    fun findByID(id: Long): AnimalCategoryEntity? {
//        return find("id", id).firstResult();
//    }
}