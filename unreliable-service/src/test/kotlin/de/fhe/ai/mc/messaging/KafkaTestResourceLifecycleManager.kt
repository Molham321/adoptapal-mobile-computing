package de.fhe.ai.mc.messaging

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector

/**
 * Test Config for Messaging/Kafka
 *
 * Kafka Streams will be handled in-memory and are thus easier to
 * use and observe.
 */

class KafkaTestResourceLifecycleManager : QuarkusTestResourceLifecycleManager {

    override fun start(): MutableMap<String, String> {
        val env = HashMap<String, String>()

        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("kafka-geocoding-request-topic"))
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("geocoding-response"))

        return env
    }

    override fun stop() {
        InMemoryConnector.clear()
    }
}