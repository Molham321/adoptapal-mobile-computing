package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDateTime

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

@ApplicationScoped
class ColorRepository: PanacheRepository<ColorEntity> {
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

//    fun getAllColors(): List<ColorEntity> {
//        return listAll();
//    }
//
//    fun findByID(id: Long): ColorEntity? {
//        return find("id", id).firstResult();
//    }
}