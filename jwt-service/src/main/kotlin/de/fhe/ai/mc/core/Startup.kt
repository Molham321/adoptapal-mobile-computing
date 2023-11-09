package de.fhe.ai.mc.core

import de.fhe.ai.mc.model.UserRepository
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
class Startup {

    @Inject
    lateinit var userRepository: UserRepository

    /*
        Helper method to add some test users to our in-memory db.
        Observes the startup event to run before anything else.
     */
    @Transactional
    fun loadUsers(@Observes evt: StartupEvent?) {
        // reset and load all test users
        userRepository.deleteAll()

        userRepository.add("scott", "boss", "admin")
        userRepository.add("jdoe", "password", "user")

    }
}