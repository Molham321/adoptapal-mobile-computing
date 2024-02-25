package de.fhe.adoptapal.resources

class TokenRequest {
    lateinit var password: String
}

class CreateUserRequest {
    lateinit var email: String
    lateinit var password: String
}

class UpdateUserRequest {
    lateinit var password: String

    var newEmail: String? = null
    var newPassword: String? = null
}

class DeleteUserRequest {
    lateinit var password: String
}

data class UsersResponse(val users: List<UserResponse>)

data class UserResponse(var id: Long, var email: String)

data class NewTokenResponse(var id: Long, var token: String, var expiresAt: Long)

