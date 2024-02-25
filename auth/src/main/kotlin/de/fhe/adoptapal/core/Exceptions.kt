package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.ErrorResponse
import jakarta.ws.rs.core.Response

class TokenInvalidatedException : Exception("a token for a user was no longer valid")
class TokenNotFoundException(val id: Long) : Exception("token was not found")
class MissingTokenClaimException: Exception("a token did not contain the validity claim")

class UserNotFoundException(val id: Long? = null, val email: String? = null) : Exception("user was not found") {
    companion object {
        fun byId(id: Long): UserNotFoundException {
            return UserNotFoundException(id, null)
        }

        fun byEmail(email: String): UserNotFoundException {
            return UserNotFoundException(null, email)
        }
    }
}
class PasswordAuthenticationException : Exception("password authentication failed")
class EmailTakenException(val email: String) : Exception("the requested email is already taken")

fun mapExceptionToResponse(ex: Exception): Response {
    return when (ex) {
        is TokenNotFoundException -> Response.status(Response.Status.UNAUTHORIZED).entity(ErrorResponse("token with `${ex.id}` not found")).build()
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
        is PasswordAuthenticationException -> Response.status(Response.Status.UNAUTHORIZED).entity(ErrorResponse("`email or password incorrect`")).build()
        is EmailTakenException -> Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse("the email `${ex.email}` is already taken")).build()
        is TokenInvalidatedException -> Response.serverError().build()
        is MissingTokenClaimException -> Response.serverError().build()
        else -> Response.serverError().build()
    }
}
