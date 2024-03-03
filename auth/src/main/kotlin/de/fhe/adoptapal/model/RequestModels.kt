package de.fhe.adoptapal.model

import jakarta.ws.rs.HeaderParam


/**
 * Class representing user credentials for authentication.
 */
class UserCredentials {
    @HeaderParam("X-User-Email")
    lateinit var email: String

    @HeaderParam("X-User-Password")
    lateinit var password: String
}

/**
 * Class representing user information for creating a new user.
 */
class CreateUser {
    lateinit var email: String
    lateinit var password: String
}

/**
 * Class representing user information for updating an existing user.
 */
class UpdateUser {
    var email: String? = null
    var password: String? = null
}

/**
 * Data class representing a response containing user information.
 */
data class UserResponse(var id: Long, var email: String)

/**
 * Data class representing a response containing information about a newly generated token.
 */
data class NewTokenResponse(var id: Long, var token: String, var expiresAt: Long)

/**
 * Data class representing an error response.
 */
data class ErrorResponse(var reason: String)
