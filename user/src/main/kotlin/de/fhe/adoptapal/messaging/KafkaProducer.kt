package de.fhe.adoptapal.messaging

import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.jboss.logging.Logger

class KafkaProducer {
    companion object {
        private val LOG: Logger = Logger.getLogger(KafkaProducer::class.java)
    }

    @Inject
    @Channel("create-user")
    lateinit var createUserEmitter: Emitter<CreateUserMessage>

    @Inject
    @Channel("update-user")
    lateinit var updateUserEmitter: Emitter<UpdateUserMessage>

    @Inject
    @Channel("delete-user")
    lateinit var deleteUserEmitter: Emitter<DeleteUserMessage>

    fun emitCreateUser(id: Long, email: String, password: String) {
        LOG.info("sending create-user topic for user with id `$id`")
        createUserEmitter.send(CreateUserMessage(id, email, password)).toCompletableFuture().join()
    }

    fun emitUpdateUser(id: Long, newEmail: String?, newPassword: String?) {
        LOG.info("sending update-user topic for user with id `$id`")
        updateUserEmitter.send(UpdateUserMessage(id, newEmail, newPassword)).toCompletableFuture().join()
    }

    fun emitDeleteUser(id: Long) {
        LOG.info("sending delete-user topic for user with id `$id`")
        deleteUserEmitter.send(DeleteUserMessage(id)).toCompletableFuture().join()
    }
}
