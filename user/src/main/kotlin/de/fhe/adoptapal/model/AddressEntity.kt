package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import java.time.LocalDateTime

@Entity
class AddressEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var street: String? = null
    var city: String? = null
    var postalCode: String? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()
    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()
    var isDeleted: Boolean = false
}