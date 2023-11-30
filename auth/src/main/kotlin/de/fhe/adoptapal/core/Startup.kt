package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.UserRepository
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@ApplicationScoped
class Startup {
    @Inject
    private lateinit var userRepository: UserRepository

    /**
     *  Helper method to add some test users to our in-memory db.
     *  Observes the startup event to run before anything else.
     */
    @Transactional
    fun loadUsers(@Observes evt: StartupEvent?) {
        // reset and load all test users
        userRepository.deleteAll()

        userRepository.add("scott", "boss")
        userRepository.add("john", "password")

    }
}