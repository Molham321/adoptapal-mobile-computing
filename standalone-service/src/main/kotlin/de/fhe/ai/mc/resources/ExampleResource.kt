package de.fhe.ai.mc.resources

import com.fasterxml.jackson.annotation.JsonView
import io.quarkus.logging.Log
import io.quarkus.logging.LoggingFilter
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.faulttolerance.CircuitBreaker
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.faulttolerance.Timeout
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.*
import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import org.jboss.resteasy.reactive.server.ServerRequestFilter
import org.jboss.resteasy.reactive.server.ServerResponseFilter
import org.jetbrains.annotations.NonBlocking
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.random.Random


@Path("/")
class ExampleResource {

    private val LOG: Logger = Logger.getLogger(LoggingFilter::class.java)

    @GET
    @Path("/reactive-hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun reactiveHello(): Uni<String> = Uni.createFrom().item("Hello from RESTEasy Reactive")

    @GET
    @Path("/reactive-multi-hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun reactiveMultiHello(): Multi<String> =
        Multi.createFrom().items("Hello 1", "Hello 2")
            .onItem().transform { it.replace(" ", " World ") }
            .onItem().transform { "- $it -" }

    @GET
    @Path("/classic-hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun classicHello() = "Hello from RESTEasy Classic"

    /*
        Custom HTTP method
     */
    @Retention(AnnotationRetention.RUNTIME)
    @HttpMethod("CHEESE")
    internal annotation class CHEESE

    @CHEESE
    @Path("/cheese-hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun cheeseHello() = "Hello, Cheese World"

    /*
        Request, Header & Form Parameter
     */
    @POST
    @Path("/params/{product}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun parameterExample(
        @RestPath product: String,
        @RestMatrix variant: String,
        @RestQuery year: String,
        @RestCookie knownCustomer: String,
        @RestHeader("X-Secret-Handshake") secretHandshake: String,
        @RestForm price: String
    ) = "$product / $variant / $year / $knownCustomer / $secretHandshake / $price"


    class Parameters {
        @RestPath var product: String? = null
        @RestMatrix var variant: String? = null
        @RestQuery var year: String? = null
        @RestCookie var knownCustomer: String? = null
        @RestHeader("X-Cheese-Secret-Handshake") var secretHandshake: String? = null
        @RestForm var price: String? = null

        override fun toString() = "Parameters(product=$product, variant=$variant, year=$year, " +
                "knownCustomer=$knownCustomer, secretHandshake=$secretHandshake, price=$price)"
    }

    @POST
    @Path("/params-class/{product}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun parameterClassExample(@BeanParam p: Parameters) = "$p"

    @GET
    @Path("/greeting/{name}/{age:\\d+}")
    fun personalisedHello(name: String, age: Int) =
        "Hello $name your age is $age"


    /*
        Custom Response
     */
    @GET
    @Path("custom-made-response")
    fun customResponse(): RestResponse<String> = RestResponse.ResponseBuilder
            .ok("Hello, World!", MediaType.TEXT_PLAIN_TYPE)
            .header("X-Custom-Header", "Header Value")
            .expires(Date.from(Instant.now().plus(Duration.ofDays(2))))
            .cookie(NewCookie.Builder("price").value("22.00€").build())
            .language(Locale.GERMAN)
            .build()


    /*
        JSON Views
     */
    class Views {
        open class Public
        class Private : Public()
    }

    class User {
        @JsonView(Views.Private::class)
        var id = 0

        @JsonView(Views.Public::class)
        var name: String? = null

        constructor(id: Int, name: String?) {
            this.id = id
            this.name = name
        }
    }

    @JsonView(Views.Public::class)
    @GET
    @Path("/public-user")
    fun userPublic() = User(42, "Mustermann")

    @JsonView(Views.Private::class)
    @GET
    @Path("/private-user")
    fun userPrivate() = User(42, "Mustermann")


    /*
        Exception Handling
     */
    @GET
    @Path("/with-exceptions")
    @NonBlocking
    fun withExceptions(@RestQuery bookName: String?) =
        when (bookName) {
            null -> throw BadRequestException()
            "unknown" -> throw NotFoundException("We didn't find that book")
            else -> Uni.createFrom().item("Nice book!")
        }

    @ServerExceptionMapper
    fun mapException(x: IllegalStateException) =
        Uni.createFrom().item(
            RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, "Illegal State: " + x.message)
        )

    @GET
    @Path("mapped-exception")
    fun triggerException(): String {
        throw java.lang.IllegalStateException("Just for the fun of it")
    }

    /*
        Request & Response Filter
        Filters should be placed in their own source file normally
     */
    // We just allow GET and POST methods
    @ServerRequestFilter(preMatching = true)
    fun httpMethodFilter(requestContext: ContainerRequestContext) : Optional<RestResponse<Void>> =
        when (requestContext.method) {
            !in arrayOf("GET", "POST", "DELETE", "PUT") -> Optional.of(RestResponse.status(Response.Status.METHOD_NOT_ALLOWED))
            else -> Optional.empty()
        }

    // Get some more infos for request for next filter
    @Context
    lateinit var httpRequest: io.vertx.core.http.HttpServerRequest

    // This filter will simply log any incoming request
    @ServerRequestFilter
    fun loggingFilter(
        requestContext: ContainerRequestContext
    ) : Response? {
        LOG.infof("Request ${requestContext.request.method} on ${requestContext.uriInfo.path} from ${httpRequest.remoteAddress()}")
        return null
    }

    // This filter will alter text responses to uppercase
    @ServerResponseFilter
    fun uppercaseFilter(responseContext: ContainerResponseContext) {
        val entity = responseContext.entity
        if (entity is String) {
            responseContext.entity = entity.uppercase(Locale.getDefault())
        }
    }

    /*
        REST Client Examples
     */
    @Path("/")
    @RegisterRestClient(configKey="remote-rest-service")
    @Produces(MediaType.TEXT_PLAIN)
    interface RemoteRestService {
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

    @RestClient
    lateinit var remoteRestService: RemoteRestService

    @GET
    @Path("/remote-greeting")
    fun remoteHello() = remoteRestService.greetingWithNameAndAge("Max", 26)

    @GET
    @Path("/remote-reactive-hello")
    fun remoteReactiveHello() = remoteRestService.remoteReactiveMultiHello()

    /*
        Fault Tolerance Examples
     */
    // Retry Example
    @GET
    @Path("unreliable")
    @Produces(MediaType.TEXT_PLAIN)
    @Retry(maxRetries = 4)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5)
    fun unreliable(): Uni<String> =
        when {
            Random.nextInt(10) > 2 -> {Log.info("Error"); throw RuntimeException("Resource failure") }
            else -> Uni.createFrom().item("Success Response")
        }

    // Timeout & Fallback Example
    @GET
    @Path("/timeout")
    @Timeout(250)
    @Fallback(fallbackMethod = "fallback")
    fun timeout(): String =
        try {
            Thread.sleep(Random.nextInt(500).toLong())
            "Not timed out!"
        }
        catch (e: InterruptedException) {
            LOG.error("Thread Interrupted Error")
            "no value"
        }

    fun fallback(): String {
        Log.info("Fallback invoked")
        return "Fallback value"
    }
}
