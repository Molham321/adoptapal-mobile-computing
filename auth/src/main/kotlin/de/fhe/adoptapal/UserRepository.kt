package de.fhe.adoptapal

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository : PanacheRepository<UserEntity> {
    fun findByUsername(username: String): UserEntity? {
        return find("username", username).firstResult()
    }
}