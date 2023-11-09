package de.fhe.ai.mc.messaging

import de.fhe.ai.mc.network.GeocodingApiClient
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.annotations.Blocking
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/*
    Processors for simple local messaging example
 */

@ApplicationScoped
class MessagingRequestProcessor {
    @Incoming("kafka-requests-topic")
    @Outgoing("responses")
    @Blocking
    @Throws(InterruptedException::class)
    fun process(request: String?): String {
        Logger.getLogger("MRP").info( "Processing $request..." )
        Thread.sleep(200)
        Logger.getLogger("MRP").info( "Processing $request finished" )

        return "Processed $request"
    }
}

@ApplicationScoped
class MessagingResponseProcessor {
    @Incoming("kafka-responses-topic")
    fun process(response: String?) =
        Logger.getLogger("MRP").info( "Received $response" )
}

/*
    Processor for inter-service geocoding example
 */

@ApplicationScoped
class GeocodingRequestProcessor {

    @Inject
    @RestClient
    lateinit var geoApi: GeocodingApiClient

    @Incoming("kafka-geocoding-request-topic")
    @Outgoing("geocoding-response")
    fun process(request: GeocodingResult?): Uni<GeocodingResult> {
        Logger.getLogger("MRP").info( "Received $request..." )

        return geoApi.reverseGeoCode(
            request?.location?.latitude,
            request?.location?.longitude )
            .onItem().ifNotNull().invoke { returnAddress ->
                returnAddress.address?.let {

                    Logger.getLogger("MRP").info( "Address $it..." )

                    val address = Address()
                    address.city = it.city
                    address.country = it.country
                    address.state = it.state
                    address.postcode = it.postcode

                    request?.address = address
                }
            }.replaceWith(Uni.createFrom().item(request))
    }
}