package de.fhe.adoptapal

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@ApplicationScoped
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    @Transactional
    fun registerUser(username: String, password: String): UserEntity {
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            // Benutzer existiert bereits
            throw IllegalArgumentException("Benutzer existiert bereits")
        }

        val newUserEntity = UserEntity()
        newUserEntity.username = username
        newUserEntity.password = password

        userRepository.persist(newUserEntity)

        return newUserEntity
    }

    fun loginUser(username: String, password: String): UserEntity? {
        val user = userRepository.findByUsername(username)

        if (user != null && user.password == password) {
            // Benutzer erfolgreich eingeloggt
            return user
        } else {
            // Login fehlgeschlagen
            return null
        }
    }
}