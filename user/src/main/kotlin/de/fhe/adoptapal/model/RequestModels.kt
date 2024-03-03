package de.fhe.adoptapal.model

import jakarta.ws.rs.HeaderParam

/**
 * Data class representing user credentials for authentication.
 *
 * @property email The user's email obtained from the "X-User-Email" header.
 * @property password The user's password obtained from the "X-User-Password" header.
 */
class UserCredentials {
    @HeaderParam("X-User-Email")
    lateinit var email: String

    @HeaderParam("X-User-Password")
    lateinit var password: String
}

/**
 * Data class representing the request body for creating a new user.
 *
 * @property username The username of the new user.
 * @property email The email of the new user.
 * @property password The password of the new user.
 * @property phoneNumber The phone number of the new user.
 * @property address The [CreateAddress] object representing the address of the new user.
 */
class CreateUser {
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var phoneNumber: String
    lateinit var address: CreateAddress
}

/**
 * Data class representing the request body for updating an existing user.
 *
 * @property username The new username for the user, or null if not updating.
 * @property phoneNumber The new phone number for the user, or null if not updating.
 * @property address The [UpdateAddress] object representing the updated address, or null if not updating.
 */
class UpdateUser {
    var username: String? = null
    var phoneNumber: String? = null
    var address: UpdateAddress? = null
}

/**
 * Data class representing the request body for creating a new address.
 *
 * @property street The street of the new address.
 * @property city The city of the new address.
 * @property postalCode The postal code of the new address.
 */
class CreateAddress {
    lateinit var street: String
    lateinit var city: String
    lateinit var postalCode: String
}

/**
 * Data class representing the request body for updating an existing address.
 *
 * @property street The new street for the address, or null if not updating.
 * @property city The new city for the address, or null if not updating.
 * @property postalCode The new postal code for the address, or null if not updating.
 */
class UpdateAddress {
    var street: String? = null
    var city: String? = null
    var postalCode: String? = null
}

/**
 * Data class representing the response body for successfully creating an authenticated user.
 *
 * @property id The ID of the created user.
 * @property email The email of the created user.
 */
class AuthUserCreatedResponse {
    var id: Long = 0
    lateinit var email: String
}

/**
 * Data class representing the response body for a user.
 *
 * @property id The ID of the user.
 * @property username The username of the user.
 * @property phoneNumber The phone number of the user.
 * @property authId The authentication ID of the user.
 * @property address The [AddressResponse] object representing the address of the user.
 */
data class UserResponse(
    var id: Long,
    val username: String,
    val phoneNumber: String,
    val authId: Long,
    val address: AddressResponse,
)

/**
 * Data class representing the response body for an address.
 *
 * @property street The street of the address.
 * @property city The city of the address.
 * @property postalCode The postal code of the address.
 */
data class AddressResponse(
    val street: String,
    val city: String,
    val postalCode: String,
)

/**
 * Data class representing the request body for creating an authenticated user.
 *
 * @property email The email of the user.
 * @property password The password of the user.
 */
data class AuthCreateUserRequest(val email: String, val password: String)

/**
 * Data class representing the response body for an error.
 *
 * @property reason The reason for the error.
 */
data class ErrorResponse(val reason: String)