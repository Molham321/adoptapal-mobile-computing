package de.fhe.ai.mc.messaging

import de.fhe.ai.mc.model.Address
import de.fhe.ai.mc.model.GeocodingResult
import de.fhe.ai.mc.model.GeocodingResultRepository
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Inject

@ApplicationScoped
class GeocodingResultProcessor {

    @Inject
    lateinit var geocodingResultRepository: GeocodingResultRepository

    @Incoming("kafka-geocoding-response-topic")
    @ReactiveTransactional
    @ActivateRequestContext
    fun process(result: GeocodingResult?): Uni<GeocodingResult> {
        Logger.getLogger("MRP").info( "Received $result..." )

        return geocodingResultRepository.findById( result?.id )
            .invoke { entity ->
                entity.address = Address()
                entity.address?.city = result?.address?.city
                entity.address?.country = result?.address?.country
                entity.address?.postcode = result?.address?.postcode
                entity.address?.state = result?.address?.state
                Logger.getLogger("MRP").info( "Result saved" )
            }
    }
}
