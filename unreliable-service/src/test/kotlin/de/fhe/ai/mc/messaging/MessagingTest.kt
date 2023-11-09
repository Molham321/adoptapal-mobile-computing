package de.fhe.ai.mc.messaging

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Any
import javax.inject.Inject
import org.awaitility.Awaitility.await

/**
 * Kafka Messaging Processor Test
 */

@QuarkusTest
@QuarkusTestResource(KafkaTestResourceLifecycleManager::class)
class MessagingTest {

    @Inject @Any
    lateinit var connector: InMemoryConnector

    @Test
    fun testMessaging() {
        // Given
        val geoIn = connector.source<GeocodingResult>("kafka-geocoding-request-topic")
        val geoOut = connector.sink<GeocodingResult>("geocoding-response")

        // When
        val geoRequest = GeocodingResult()
        geoRequest.location = Location("50", "11")
        geoIn.send(geoRequest)
        await().until(geoOut::received) { t -> t.size == 1 }

        // Then
        Assertions.assertEquals(1, geoOut.received().size)

        val geoResponse = geoOut.received()[0].payload

        Assertions.assertNotNull(geoResponse.address)
        Assertions.assertEquals("Erfurt", geoResponse.address?.city)
    }
}