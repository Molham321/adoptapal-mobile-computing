package de.fhe.adoptapal.model

import de.fhe.adoptapal.core.PasswordUtils
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional

@Entity
@UserDefinition
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Username
    lateinit var email: String

    @Password
    lateinit var password: String

    @Roles
    lateinit var role: String

    override fun toString(): String {
        return "User(id=$id, email=$email, password=$password)"
    }
}

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {
    @Transactional
    fun add(email: String, password: String) {
        val userEntity = UserEntity()
        userEntity.email = email
        userEntity.password = PasswordUtils.hashPassword(password)

        persist(userEntity)
    }

    fun findByEmail(name: String) = find("email", name).firstResult()
}