package de.fhe.adoptapal.model

data class RequestSubject(val id: Long, val email: String, val password: String)

class TokenRequest {
    lateinit var email: String
    lateinit var password: String
}

class CreateUserRequest {
    lateinit var email: String
    lateinit var password: String
}

class UpdateUserRequest {
    lateinit var email: String
    lateinit var password: String

    var newEmail: String? = null
    var newPassword: String? = null
}

class DeleteUserRequest {
    lateinit var email: String
    lateinit var password: String
}

data class UserResponse(var id: Long, var email: String)

data class NewTokenResponse(var id: Long, var token: String, var expiresAt: Long)

data class ErrorResponse(var reason: String)
