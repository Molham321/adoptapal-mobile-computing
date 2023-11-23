package de.fhe.adoptapal

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserResource {

    @Inject
    lateinit var userService: UserService

    @POST
    @Path("/register")
    fun register(user: User): User {
        return userService.registerUser(user.username!!, user.password!!)
    }

    @POST
    @Path("/login")
    fun login(user: User): User? {
        return userService.loginUser(user.username!!, user.password!!)
    }
}