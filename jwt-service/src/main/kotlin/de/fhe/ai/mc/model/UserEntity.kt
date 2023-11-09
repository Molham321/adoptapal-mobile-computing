package de.fhe.ai.mc.model

import de.fhe.ai.mc.core.PasswordUtils
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@UserDefinition
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null
    @Username
    lateinit var username: String
    @Password
    lateinit var password: String
    @Roles
    lateinit var role: String

    override fun toString(): String {
        return "User(id=$id, username=$username, password=$password, role=$role)"
    }
}

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {
    fun add(username: String, password: String, role: String) {
        val userEntity = UserEntity()
        userEntity.username = username
        userEntity.password = PasswordUtils.hashPassword(password)
        userEntity.role = role

        persist(userEntity)
    }

    fun findByName(name: String) = find("username", name).firstResult()
}
