package de.fhe.adoptapal.model

import io.quarkus.security.jpa.Username
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
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