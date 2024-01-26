package de.fhe.adoptapal.model

import de.fhe.adoptapal.core.PasswordUtils
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
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
        return "User(id=$id, email=$email, password=$password, role=$role)"
    }

    class Role {
        companion object {
            const val USER: String = "user"
            const val ADMIN: String = "admin"
        }
    }
}

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {
    @Transactional
    fun create(email: String, password: String, role: String = UserEntity.Role.USER): UserEntity {
        val userEntity = UserEntity()
        userEntity.email = email
        userEntity.password = PasswordUtils.hashPassword(password)
        userEntity.role = role

        persist(userEntity)
        flush()
        return userEntity
    }

    @Transactional
    fun updatePasswordById(id: Long, password: String) {
        update(
            "password=:password where id=:id",
            Parameters()
                .and("password", PasswordUtils.hashPassword(password))
                .and("id", id)
        )
    }

    @Transactional
    fun updateRoleById(id: Long, role: String) {
        update(
            "role=:role where id=:id",
            Parameters()
                .and("role", role)
                .and("id", id)
        )
    }

    @Transactional
    fun updatePasswordByEmail(email: String, password: String) {
        update(
            "password=:password where email=:email",
            Parameters()
                .and("password", PasswordUtils.hashPassword(password))
                .and("email", email)
        )
    }

    @Transactional
    fun updateRoleByEmail(email: String, role: String) {
        update(
            "role=:role where email=:email",
            Parameters()
                .and("role", role)
                .and("email", email)
        )
    }

    @Transactional
    fun deleteByEmail(email: String) {
        delete("email", email)
    }

    fun findByEmail(name: String): UserEntity? = find("email", name).firstResult()
}