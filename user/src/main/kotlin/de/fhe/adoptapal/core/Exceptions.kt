package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.ErrorResponse
import jakarta.ws.rs.core.Response

/**
 * Exception indicating that a user was not found.
 *
 * @property id The ID of the user, if applicable.
 * @property email The email of the user, if applicable.
 * @constructor Creates a [UserNotFoundException].
 */
class UserNotFoundException(val id: Long? = null, val email: String? = null) : Exception("user was not found") {
    companion object {
        /**
         * Creates a [UserNotFoundException] based on the user's ID.
         *
         * @param id The ID of the user.
         * @return A [UserNotFoundException] instance.
         */
        fun byId(id: Long): UserNotFoundException {
            return UserNotFoundException(id, null)
        }
    }
}

/**
 * Exception indicating that token authentication failed.
 */
class TokenAuthenticationException : Exception("token authentication failed")

/**
 * Exception indicating that password authentication failed.
 */
class PasswordAuthenticationException : Exception("password authentication failed")

/**
 * Exception indicating that the requested email is already taken.
 *
 * @property email The email that is already taken.
 * @constructor Creates an [EmailTakenException].
 */
class EmailTakenException(val email: String) : Exception("the requested email is already taken")


/**
 * Maps exceptions to appropriate JAX-RS responses.
 *
 * @param ex The exception to map.
 * @return The corresponding JAX-RS [Response] object.
 */
fun mapExceptionToResponse(ex: Exception): Response {
    return when (ex) {
        is UserNotFoundException -> {
            val ident = if (ex.id != null) {
                "user with id `${ex.id}`"
            } else if (ex.email != null) {
                "user with email `${ex.email}`"
            } else {
                "user"
            }

            Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse("$ident not found")).build()
        }

        is PasswordAuthenticationException -> Response.status(Response.Status.UNAUTHORIZED)
            .entity(ErrorResponse("email or password incorrect")).build()

        is EmailTakenException -> Response.status(Response.Status.BAD_REQUEST)
            .entity(ErrorResponse("the email `${ex.email}` is already taken")).build()

        is TokenAuthenticationException -> Response.status(Response.Status.UNAUTHORIZED)
            .entity(ErrorResponse("trying to access restricted resource")).build()

        else -> Response.serverError().build()
    }
}
