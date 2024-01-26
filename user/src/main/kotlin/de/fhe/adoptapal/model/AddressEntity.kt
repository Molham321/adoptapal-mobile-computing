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

@ApplicationScoped
class AddressRepository : PanacheRepository<AddressEntity> {
    @Transactional
    fun add(
        street: String,
        city: String,
        postalCode: String
    ): AddressEntity {
        val addressEntity = AddressEntity()
        addressEntity.street = street
        addressEntity.city = city
        addressEntity.postalCode = postalCode

        addressEntity.createdTimestamp = LocalDateTime.now()
        addressEntity.lastChangeTimestamp = LocalDateTime.now()
        addressEntity.isDeleted = false

        persist(addressEntity)
        return addressEntity
    }

    @Transactional
    fun updateAddress(addressEntity: AddressEntity) {
        addressEntity.street?.let {
            addressEntity.city?.let { it1 ->
                addressEntity.postalCode?.let { it2 ->
                    addressEntity.id?.let { it3 ->
                        update(
                            "street=?1, city=?2, postalCode=?3 where id = ?4",
                            it,
                            it1,
                            it2,
                            it3
                        )
                    }
                }
            }
        }
    }

    @Transactional
    fun deleteAddress(id: Long) {
        deleteById(id);
    }
}