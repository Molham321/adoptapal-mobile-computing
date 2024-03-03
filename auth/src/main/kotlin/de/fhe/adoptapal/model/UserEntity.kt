package de.fhe.adoptapal.model

import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * Entity class representing a user.
 */
@Entity
@UserDefinition
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Username
    @Column(unique = true)
    lateinit var email: String

    @Password
    lateinit var password: String

    @Roles
    lateinit var role: String

    enum class Role {
        USER,
        ADMIN,
    }
}