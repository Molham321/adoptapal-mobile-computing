package de.fhe.adoptapal

import com.fasterxml.jackson.annotation.JsonView
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
    @Path("/register/{username}/{password}")
    fun register(username: String, password: String): UserEntity {
        return userService.registerUser(username, password)
    }

    @JsonView(Views.Public::class)
    @GET
    @Path("/login/{username}/{password}")
    fun login(username: String, password: String): UserEntity? {
        return userService.loginUser(username, password)
    }
}