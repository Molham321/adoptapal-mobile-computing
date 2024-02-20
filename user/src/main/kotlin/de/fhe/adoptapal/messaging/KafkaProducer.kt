package de.fhe.adoptapal.messaging

import de.fhe.adoptapal.resources.UserResource
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.jboss.logging.Logger


class KafkaProducer {

    companion object {
        private val LOG: Logger = Logger.getLogger(KafkaProducer::class.java)
    }

    @Inject
    @Channel("post-out")
    lateinit var emitter: Emitter<Long>

    //Logging
    fun sendPost(userId: Long?) {
        LOG.info("sendPost methode")
        emitter.send(userId)
    }
}