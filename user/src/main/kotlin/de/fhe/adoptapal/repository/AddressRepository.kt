package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.AddressEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDateTime

@ApplicationScoped
class AddressRepository : PanacheRepository<AddressEntity> {
    @Transactional
    fun create(street: String, city: String, postalCode: String): AddressEntity {
        val entity = AddressEntity()
        entity.street = street
        entity.city = city
        entity.postalCode = postalCode

        entity.createdAt = LocalDateTime.now()

        persist(entity)
        flush()

        return entity
    }

    @Transactional
    fun find(id: Long) = find("id", id).firstResult()

    @Transactional
    fun update(id: Long, newStreet: String?, newCity: String?, newPostalCode: String?) {
        var fields = ""
        val params = Parameters.with("id", id)

        newStreet?.let {
            fields += ", street = :street"
            params.and("street", it)
        }
        newCity?.let {
            fields += ", city = :city"
            params.and("city", it)
        }
        newPostalCode?.let {
            fields += ", postalCode = :postalCode"
            params.and("postalCode", it)
        }

        fields = fields.substring(2)
        update("$fields where id = :id", params)
    }

    @Transactional
    fun delete(id: Long) = deleteById(id)
}