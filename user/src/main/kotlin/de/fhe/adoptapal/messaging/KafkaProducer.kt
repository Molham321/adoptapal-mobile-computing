package de.fhe.adoptapal.messaging

import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter


class KafkaProducer {
    @Inject
    @Channel("post-out")
    lateinit var emitter: Emitter<Long>

    //Logging
    fun sendPost(userId: Long?) {
        emitter.send(userId)
    }
}