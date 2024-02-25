package de.fhe.adoptapal.model

import de.fhe.adoptapal.core.PasswordUtils
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.transaction.Transactional

@Entity
@UserDefinition
class UserEntity {
    @Id
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

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {
    @Transactional
    fun create(id: Long, email: String, password: String, role: UserEntity.Role = UserEntity.Role.USER): UserEntity {
        val entity = UserEntity()
        entity.id = id
        entity.email = email
        entity.password = PasswordUtils.hashPassword(password)
        entity.role = role.name

        persist(entity)
        flush()
        return entity
    }

    @Transactional
    fun find(id: Long): UserEntity? = find("id", id).firstResult()

    @Transactional
    fun find(email: String): UserEntity? = find("email", email).firstResult()

    @Transactional
    fun update(id: Long, newEmail: String?, newPassword: String?, newRole: UserEntity.Role?) {
        var fields = ""
        val params = Parameters.with("id", id)

        newEmail?.let {
            fields += ", email = :email"
            params.and("email", it)
        }
        newPassword?.let {
            fields += ", password = :password"
            params.and("password", PasswordUtils.hashPassword(it))
        }
        newRole?.let {
            fields += ", role = :role"
            params.and("role", it.name)
        }

        fields = fields.substring(2)
        update("$fields where id = :id", params)
    }

    @Transactional
    fun delete(id: Long) = deleteById(id)
}
