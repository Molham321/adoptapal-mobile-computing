//package de.fhe.adoptapal
//
//import io.quarkus.runtime.StartupEvent
//import jakarta.enterprise.context.ApplicationScoped
//import jakarta.enterprise.event.Observes
//
//@ApplicationScoped
//class Application {
//
//    companion object {
//        private val userList = ArrayList<User>()
//
//        fun addUser( user: User ) = userList.add(user)
//        fun findUser( username: String ) = userList.find { user -> user.username == username }
//    }
//    fun loadUsers(@Observes evt: StartupEvent?) {
//        addUser(User("scott", "boss", "scott@test.com"))
//        addUser(User("jdoe", "password", "jdoe@test.com"))
//    }
//}