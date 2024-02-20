package de.fhe.adoptapal.messaging

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger


@ApplicationScoped
class KafkaConsumer {
    companion object {
        private val LOG: Logger = Logger.getLogger(KafkaConsumer::class.java)
    }
    @Incoming("user-to-auth-delete")
    fun receiveDelete(userId: String): String {
        LOG.info("Incoming from kafka... user with ID: $userId was deleted" )
        return userId
    }

    @Incoming("user-to-auth-Register")
    fun receiveRegister(userId: String): String {
        LOG.info("Incoming from kafka... user with ID: $userId was Register" )
        return userId
    }
}