package de.fhe.ai.mc.resources

import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.util.*
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/messaging")
class MessagingResource {

    @Channel("requests")
    lateinit var emitter: Emitter<String>

    @POST
    fun postSomething(): String {
        val uuid = UUID.randomUUID().toString()
        emitter.send(uuid)
        return uuid
    }
}

