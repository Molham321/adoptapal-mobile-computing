package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.AddressEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDateTime

/**
 * Repository class for managing address data operations in the database.
 *
 * @constructor Creates an [AddressRepository] instance.
 */
@ApplicationScoped
class AddressRepository : PanacheRepository<AddressEntity> {

    /**
     * Creates a new address in the database.
     *
     * @param street The street of the new address.
     * @param city The city of the new address.
     * @param postalCode The postal code of the new address.
     * @return The created [AddressEntity] instance.
     */
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

    /**
     * Updates address information in the database.
     *
     * @param id The ID of the address to update.
     * @param newStreet The new street to update, or null if not updating.
     * @param newCity The new city to update, or null if not updating.
     * @param newPostalCode The new postal code to update, or null if not updating.
     */
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

    /**
     * Deletes an address from the database by ID.
     *
     * @param id The ID of the address to delete.
     */
    @Transactional
    fun delete(id: Long) = deleteById(id)
}