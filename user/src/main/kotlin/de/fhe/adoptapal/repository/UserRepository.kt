package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.AddressEntity
import de.fhe.adoptapal.model.UserEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.xml.bind.ValidationException
import java.time.LocalDateTime

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {
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

    @Transactional
    fun find(id: Long): UserEntity? = find("id", id).firstResult()

    @Transactional
    fun find(email: String): UserEntity? = find("email", email).firstResult()

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

        // TODO: address id

        fields = fields.substring(2)
        update("$fields where id = :id", params)
    }

    @Transactional
    fun delete(id: Long) = deleteById(id)
}
