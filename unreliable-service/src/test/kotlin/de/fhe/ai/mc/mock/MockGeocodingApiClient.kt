package de.fhe.ai.mc.mock

import de.fhe.ai.mc.network.GeocodeReturnAddress
import de.fhe.ai.mc.network.GeocodeReturnAddressContainer
import de.fhe.ai.mc.network.GeocodingApiClient
import io.quarkus.test.Mock
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.Path

/**
 * Geo API Mock
 */

@Mock
@ApplicationScoped
@RestClient
@Path("geo-mock")
class MockGeocodingApiClient : GeocodingApiClient {

    private val logger = Logger.getLogger(MockGeocodingApiClient::class.java.name)

    override fun reverseGeoCode(
        latitude: String?,
        longitude: String?,
        format: String
    ): Uni<GeocodeReturnAddressContainer> {

        logger.info("Mock called")

        val address = GeocodeReturnAddress()
        address.city = "Erfurt"
        address.country = "Germany"
        address.state = "Thuringia"
        address.postcode = "99085"

        val container = GeocodeReturnAddressContainer()
        container.fullAddressString = "Full Mock Address"
        container.address = address

        return Uni.createFrom().item(container)

    }
}