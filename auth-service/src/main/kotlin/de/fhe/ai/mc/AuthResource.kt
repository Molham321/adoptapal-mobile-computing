package de.fhe.ai.mc

import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response

@Path("/")
class AuthResource {

    @GET
    @Path("login")
    fun login(@Context headers: HttpHeaders): Response {
        val authHeaders = headers.getRequestHeader( HttpHeaders.AUTHORIZATION )
        if( authHeaders.size == 1 && authHeaders[0].startsWith("Basic ") )
        {
            // Split `Basic <authstring>`, then decode auth string
            val plainAuthHeader = Base64.getDecoder().decode( authHeaders[0].substringAfter(" ") )
            // Split decoded auth string, e.g. scott:boss
            val authFields = String(plainAuthHeader).split(":")

            // Find user for given username
            val user = Application.findUser( authFields[0] )

            // If we have a user with such a username, then check password
            user?.let {
                val passwordOk = (authFields[1] == it.password )

                // If ok, then answer with HTTP Status 200
                if( passwordOk )
                    return Response.ok().build()
            }
        }

        // If we end up here, some checks above have not been successful. Thus, the user is not authorized.
        // Answer with HTTP Status 401
        return Response.status( Response.Status.UNAUTHORIZED ).build()
    }

}