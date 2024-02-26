package de.fhe.adoptapal.model

import jakarta.ws.rs.HeaderParam

class UserCredentials {
    @HeaderParam("X-User-Email")
    lateinit var email: String

    @HeaderParam("X-User-Password")
    lateinit var password: String
}

class CreateUserRequest {
    lateinit var email: String
    lateinit var password: String
}

class UpdateUserRequest {
    var email: String? = null
    var password: String? = null
}

data class UserResponse(var id: Long, var email: String)

data class NewTokenResponse(var id: Long, var token: String, var expiresAt: Long)

data class ErrorResponse(var reason: String)
