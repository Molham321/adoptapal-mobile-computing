package de.fhe.ai.mc.resources

import io.smallrye.mutiny.Uni
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/base")
class BaseResource {

    /*
        Plain Text Endpoints
     */
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String = "Hello from RESTEasy Reactive - Blocked - (Unreliable Service)"

    @GET
    @Path("/hello-async")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloAsync(): Uni<String> = Uni.createFrom().item("Hello from RESTEasy Reactive - Async - (Unreliable Service)")

    /*
        JSON Endpoints
     */
    val objectList = listOf( MyObject("Max"), MyObject("Hans"), MyObject("Susi") )

    @GET
    @Path("/objects")
    @Produces(MediaType.APPLICATION_JSON)
    fun getObjects(): List<MyObject> = objectList

    @GET
    @Path("/objects-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun getObjectsAsync(): Uni<List<MyObject>> = Uni.createFrom().item( objectList )
}

