package de.fhe.ai.mc.resources

import io.smallrye.mutiny.Uni
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/base")
class BaseResource {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String = "Hello from RESTEasy Reactive - Blocked - (Dependent Service)"

    @GET
    @Path("/hello-async")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloAsync(): Uni<String> = Uni.createFrom().item("Hello from RESTEasy Reactive - Async - (Dependent Service)")
}

