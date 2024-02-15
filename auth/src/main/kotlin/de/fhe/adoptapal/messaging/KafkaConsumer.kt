package de.fhe.adoptapal.messaging

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming


@ApplicationScoped
class KafkaConsumer {
    @Incoming("post-out")
    fun receive(userId: Long): Long {
        System.out.println("Received userId: " + userId.toString())

        //Do something with the ID
        //...

        return userId
    }
}