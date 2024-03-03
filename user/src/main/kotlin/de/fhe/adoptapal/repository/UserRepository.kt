package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.AddressEntity
import de.fhe.adoptapal.model.UserEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.xml.bind.ValidationException
import java.time.LocalDateTime

/**
 * Repository class for managing user data operations in the database.
 *
 * @constructor Creates a [UserRepository] instance.
 */
@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {

    /**
     * Creates a new user in the database.
     *
     * @param username The username of the new user.
     * @param phoneNumber The phone number of the new user.
     * @param address The [AddressEntity] representing the address of the new user.
     * @param authId The authentication ID associated with the new user.
     * @return The created [UserEntity] instance.
     * @throws ValidationException if the data fails validation.
     */
    @Transactional
    fun create(username: String, phoneNumber: String, address: AddressEntity, authId: Long): UserEntity {
        val entity = UserEntity()

        entity.username = username
        entity.phoneNumber = phoneNumber
        entity.authId = authId
        entity.address = address
        entity.createdAt = LocalDateTime.now()

        persist(entity)
        flush()

        return entity;
    }

    /**
     * Retrieves a user by their ID from the database.
     *
     * @param id The ID of the user to retrieve.
     * @return The [UserEntity] corresponding to the given ID, or null if not found.
     */
    @Transactional
    fun find(id: Long): UserEntity? = find("id", id).firstResult()

    /**
     * Updates user information in the database.
     *
     * @param id The ID of the user to update.
     * @param newUsername The new username to update, or null if not updating.
     * @param newPhoneNumber The new phone number to update, or null if not updating.
     */
    @Transactional
    fun update(id: Long, newUsername: String?, newPhoneNumber: String?) {
        var fields = ""
        val params = Parameters.with("id", id)

        newUsername?.let {
            fields += ", username = :username"
            params.and("username", it)
        }
        newPhoneNumber?.let {
            fields += ", phoneNumber = :phoneNumber"
            params.and("phoneNumber", it)
        }

        fields = fields.substring(2)
        update("$fields where id = :id", params)
    }

    /**
     * Deletes a user from the database by ID.
     *
     * @param id The ID of the user to delete.
     */
    @Transactional
    fun delete(id: Long) = deleteById(id)
}
