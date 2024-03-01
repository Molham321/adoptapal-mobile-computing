package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.ErrorResponse
import jakarta.ws.rs.core.Response

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
class TokenAuthenticationException : Exception("token authentication failed")
class PasswordAuthenticationException : Exception("password authentication failed")
class EmailTakenException(val email: String) : Exception("the requested email is already taken")

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
        is PasswordAuthenticationException -> Response.status(Response.Status.UNAUTHORIZED).entity(ErrorResponse("email or password incorrect")).build()
        is EmailTakenException -> Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse("the email `${ex.email}` is already taken")).build()
        is TokenAuthenticationException -> Response.status(Response.Status.UNAUTHORIZED).entity(ErrorResponse("trying to access restricted resource")).build()
        else -> Response.serverError().build()
    }
}
