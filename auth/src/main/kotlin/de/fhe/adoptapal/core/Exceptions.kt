package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.ErrorResponse
import jakarta.ws.rs.core.Response

/**
 * Exception thrown when a token for a user is no longer valid.
 */
class TokenInvalidatedException : Exception("a token for a user was no longer valid")

/**
 * Exception thrown when a token is not found, optionally with an associated ID.
 * @param id The ID of the token, if available.
 */
class TokenNotFoundException(val id: Long?) : Exception("token was not found")

/**
 * Exception thrown when a token does not contain the required validity claim.
 */
class MissingTokenClaimException : Exception("a token did not contain the validity claim")

/**
 * Exception thrown when a user is not found, optionally with an associated ID or email.
 * @param id The ID of the user, if available.
 * @param email The email of the user, if available.
 */
class UserNotFoundException(val id: Long? = null, val email: String? = null) : Exception("user was not found") {
    companion object {

        /**
         * Factory method to create a UserNotFoundException by ID.
         * @param id The ID of the user.
         * @return UserNotFoundException instance.
         */
        fun byId(id: Long): UserNotFoundException {
            return UserNotFoundException(id, null)
        }

        /**
         * Factory method to create a UserNotFoundException by email.
         * @param email The email of the user.
         * @return UserNotFoundException instance.
         */
        fun byEmail(email: String): UserNotFoundException {
            return UserNotFoundException(null, email)
        }
    }
}

/**
 * Exception thrown when token authentication fails.
 */
class TokenAuthenticationException : Exception("token authentication failed")

/**
 * Exception thrown when password authentication fails.
 */
class PasswordAuthenticationException : Exception("password authentication failed")

/**
 * Exception thrown when attempting to use an email that is already taken.
 * @param email The email that is already taken.
 */
class EmailTakenException(val email: String) : Exception("the requested email is already taken")

/**
 * Utility function to map exceptions to appropriate JAX-RS Response instances.
 * @param ex The exception to be mapped.
 * @return JAX-RS Response representing the mapped exception.
 */
fun mapExceptionToResponse(ex: Exception): Response {
    return when (ex) {
        is TokenNotFoundException -> {
            val ident = if (ex.id != null) {
                "token with `${ex.id}`"
            } else {
                "token"
            }

            Response.status(Response.Status.UNAUTHORIZED).entity(ErrorResponse("$ident not found")).build()
        }

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

        is TokenInvalidatedException -> Response.status(Response.Status.UNAUTHORIZED)
            .entity(ErrorResponse("trying to access restricted resource")).build()

        is MissingTokenClaimException -> Response.serverError().build()
        else -> Response.serverError().build()
    }
}
