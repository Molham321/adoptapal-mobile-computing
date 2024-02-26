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
    var email: String? = null
    var password: String? = null
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

data class AuthUpdateUserRequest(val email: String?, val password: String?)

data class ErrorResponse(val reason: String)