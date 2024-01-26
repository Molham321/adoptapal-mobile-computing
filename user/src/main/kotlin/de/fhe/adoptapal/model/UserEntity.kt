package de.fhe.adoptapal.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.security.jpa.Username
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.transaction.Transactional
import java.time.LocalDateTime

@Entity
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Username
    var username: String? = null

    var email: String? = null
    var phoneNumber: String? = null

    var createdTimestamp: LocalDateTime = LocalDateTime.now()
    var lastChangeTimestamp: LocalDateTime = LocalDateTime.now()
    var isDeleted: Boolean = false

    var addressID: Long? = null
}

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
}