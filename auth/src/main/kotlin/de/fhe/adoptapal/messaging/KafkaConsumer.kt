package de.fhe.adoptapal.messaging

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger


@ApplicationScoped
class KafkaConsumer {
    companion object {
        private val LOG: Logger = Logger.getLogger(KafkaConsumer::class.java)
    }
    @Incoming("post-out")
    fun receive(userId: Long): Long {
        LOG.info("Incoming from kafka!$userId")

        //Do something with the ID
        //...

        return userId
    }
}