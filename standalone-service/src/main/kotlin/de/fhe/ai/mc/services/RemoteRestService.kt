package de.fhe.ai.mc.services

import io.smallrye.mutiny.Multi
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@Path("/")
@RegisterRestClient(configKey="remote-rest-service")
@Produces(MediaType.TEXT_PLAIN)
interface RemoteRestService2 {
    @GET
    @Path("/reactive-multi-hello")
    fun remoteReactiveMultiHello(): Multi<String>

    @GET
    @Path("/greeting/{name}/{age}")
    fun greetingWithNameAndAge(
        @PathParam("name") name: String,
        @PathParam("age") age: Int
    ): String
}