package de.fhe.adoptapal.model

import jakarta.ws.rs.HeaderParam

class UserCredentials {
    @HeaderParam("X-User-Email")
    lateinit var email: String

    @HeaderParam("X-User-Password")
    lateinit var password: String
}

class CreateUser {
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var phoneNumber: String
    lateinit var address: CreateAddress
}

class UpdateUser {
    var username: String? = null
    var phoneNumber: String? = null
    var address: UpdateAddress? = null
}

class CreateAddress {
    lateinit var street: String
    lateinit var city: String
    lateinit var postalCode: String
}

class UpdateAddress {
    var street: String? = null
    var city: String? = null
    var postalCode: String? = null
}

class AuthUserCreatedResponse {
    var id: Long = 0
    lateinit var email: String
}

data class UserResponse(
    var id: Long,
    val username: String,
    val phoneNumber: String,
    val authId: Long,
    val address: AddressResponse,
)

data class AddressResponse(
    val street: String,
    val city: String,
    val postalCode: String,
)

data class AuthCreateUserRequest(val email: String, val password: String)

data class ErrorResponse(val reason: String)