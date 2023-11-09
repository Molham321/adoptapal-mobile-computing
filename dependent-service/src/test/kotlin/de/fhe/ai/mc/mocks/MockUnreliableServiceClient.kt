package de.fhe.ai.mc.mocks

import de.fhe.ai.mc.model.ValueWithIpAddress
import de.fhe.ai.mc.network.UnreliableServiceClient
import io.quarkus.test.Mock
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.Path

/**
 * Mock for our unreliable service REST client
 */

@Mock
@ApplicationScoped
@RestClient
@Path("api-mock")
class MockUnreliableServiceClient : UnreliableServiceClient {

    private val logger = Logger.getLogger(MockUnreliableServiceClient::class.java.name)

    override fun unreliableSingleValueAsync(): Uni<ValueWithIpAddress> {
        logger.info("Mock called")

        return Uni.createFrom().item(ValueWithIpAddress("Value", "127.0.0.1"))
    }

    override fun unreliableMultipleValuesAsync(n: Int): Uni<List<ValueWithIpAddress>> {
        logger.info("Mock called")

        val list = ArrayList<ValueWithIpAddress>()
        list.add(ValueWithIpAddress("Value", "127.0.0.1"))
        return Uni.createFrom().item(list)
    }
}