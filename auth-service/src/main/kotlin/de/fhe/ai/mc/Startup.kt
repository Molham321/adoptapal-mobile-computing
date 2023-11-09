package de.fhe.ai.mc

import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Application {

    companion object {
        private val userList = ArrayList<User>()

        fun addUser( user: User ) = userList.add(user)
        fun findUser( username: String ) = userList.find { user -> user.username == username }
    }

    /*
        Helper method to add some test users.
        Observes the startup event to run before anything else.
     */
    fun loadUsers(@Observes evt: StartupEvent?) {
        addUser(User("scott", "boss", "admin"))
        addUser(User("jdoe", "password", "user"))
    }
}