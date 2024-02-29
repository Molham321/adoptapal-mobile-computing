package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

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

@Transactional
@ApplicationScoped
class AnimalRepository : PanacheRepository<AnimalEntity> {
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

//    fun getAllAnimals(): List<AnimalEntity> {
//        return listAll();
//    }
//
//    fun findByID(id: Long): AnimalEntity? {
//        return find("id", id).firstResult();
//    }

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