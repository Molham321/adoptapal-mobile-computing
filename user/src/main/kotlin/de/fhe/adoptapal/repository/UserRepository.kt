package de.fhe.adoptapal.repository

import de.fhe.adoptapal.model.UserEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.xml.bind.ValidationException
import java.time.LocalDateTime

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {
    fun add(
        username: String,
        email: String,
        addressID: Long?,
        phoneNumber: String?
    ) {
        val userEntity = UserEntity()
        userEntity.username = username
        userEntity.email = email
        userEntity.phoneNumber = phoneNumber

        userEntity.createdTimestamp = LocalDateTime.now()
        userEntity.lastChangeTimestamp = LocalDateTime.now()
        userEntity.isDeleted = false

        userEntity.addressID = addressID

        persist(userEntity)
    }
    fun findByEmail(email: String): UserEntity? = find("email", email).firstResult()

    @Transactional
    fun createUser(userEntity: UserEntity) {
        persist(userEntity)
    }
    @Transactional
    fun updateUser(userEntity: UserEntity) {
        userEntity.username?.let {
            userEntity.email?.let { it1 ->
                userEntity.phoneNumber?.let { it2 ->
                    userEntity.id?.let { it3 ->
                        update(
                            "username=?1, email=?2, phoneNumber=?3 where id = ?4",
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
    fun deleteUser(id: Long) {
        delete("id", id)
    }

    fun validateUser(user: UserEntity) {
        if (user.username.isNullOrEmpty() || user.email.isNullOrEmpty()) {
            throw ValidationException("Username and email are required")
        }
    }

    fun updateExistingUser(existingUser: UserEntity, updatedUser: UserEntity) {
        existingUser.apply {
            username = updatedUser.username.takeUnless { it.isNullOrEmpty() } ?: username
            if (updatedUser.email?.isNotEmpty() == true) {
                existingUser.id?.let { validateEmailAvailability(it, updatedUser.email!!) }
                email = updatedUser.email
            }
            phoneNumber = updatedUser.phoneNumber.takeUnless { it.isNullOrEmpty() } ?: phoneNumber
            lastChangeTimestamp = LocalDateTime.now()

            // Todo updtae Address
        }
    }

    fun validateEmailAvailability(userId: Long, email: String) {
        this.findByEmail(email)?.let {
            if (it.id != userId) {
                throw ValidationException("Email is already in use by another user")
            }
        }
    }
}