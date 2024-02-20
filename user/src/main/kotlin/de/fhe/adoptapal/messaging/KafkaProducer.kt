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
    @Channel("user-to-auth-delete")
    lateinit var DeleteEmitter: Emitter<String>

    @Inject
    @Channel("user-to-auth-Register")
    lateinit var RegisterEmitter: Emitter<String>

    //Logging
    fun sendDeletePost(userId: Long?) {
        LOG.info("sendPost methode")
        DeleteEmitter.send(userId.toString())
    }

    fun sendRegisterPost(userId: Long?) {
        LOG.info("sendPost methode")
        RegisterEmitter.send(userId.toString())
    }
}