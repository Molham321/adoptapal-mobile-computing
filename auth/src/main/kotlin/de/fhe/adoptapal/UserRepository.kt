package de.fhe.adoptapal

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository : PanacheRepository<User> {
    fun findByUsername(username: String): User? {
        return find("username", username).firstResult()
    }
}