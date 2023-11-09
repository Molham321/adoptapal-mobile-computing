package de.fhe.ai.mc.resources

import de.fhe.ai.mc.model.GeocodingResult
import de.fhe.ai.mc.model.GeocodingResultRepository
import de.fhe.ai.mc.model.Location
import io.opentelemetry.context.Context
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.TracingMetadata
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.messaging.Metadata
import java.util.*
import java.util.logging.Logger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.random.Random


@Path("geocoding")
class GeocodingResource {

    @Inject
    lateinit var geocodingResultRepository: GeocodingResultRepository

    @Channel("geocoding-request")
    lateinit var emitter: Emitter<GeocodingResult>

    private val logger = Logger.getLogger(GeocodingResource::class.java.name)

    @GET
    @Path("/{requestId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getGeocodingResult( @PathParam("requestId") requestId: Long ) : Uni<Response> {
        return geocodingResultRepository.findById(requestId)
            .onItem().ifNotNull().transform { entity -> Response.ok(entity).build() }
            .onItem().ifNull().continueWith( Response.status(Response.Status.NOT_FOUND).build() )
    }

    @POST
    @ReactiveTransactional
    fun postSomething(
        @QueryParam("lat") @DefaultValue("") latitude: String,
        @QueryParam("long") @DefaultValue("") longitude: String,
    ): Uni<Response>
    {
        val geocodingResult = GeocodingResult()

        // Check if the request contained location coordinates
        if( latitude.isNotEmpty() && longitude.isNotEmpty())
        {
            geocodingResult.location = Location(latitude,longitude)
        }
        else {
            geocodingResult.location = Location(
                latitude = "${Random.nextDouble(44.0000, 53.0000)}",
                longitude = "${Random.nextDouble(6.5000, 29.0000)}"
            )
        }

        // Save Request, send it to Kafka and send a reply to client
        return geocodingResultRepository.persist( geocodingResult )
            .invoke { _ -> logger.info("Request saved") }
            .invoke { it -> emitter.send(Message.of(it, Metadata.of(TracingMetadata.withPrevious(Context.current())))) }
            .invoke { _ -> logger.info("Request sent") }
            .map { Response.accepted( it ).build() }
    }
}
