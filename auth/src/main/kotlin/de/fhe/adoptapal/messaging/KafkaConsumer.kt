package de.fhe.adoptapal.messaging

import de.fhe.adoptapal.core.UserBean
import de.fhe.adoptapal.model.UserEntity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger

@ApplicationScoped
class KafkaConsumer {
    companion object {
        private val LOG: Logger = Logger.getLogger(KafkaConsumer::class.java)
    }

    @Inject
    private lateinit var userBean: UserBean

    @Incoming("create-user")
    private fun receiveCreateUser(msg: CreateUserMessage): CreateUserMessage {
        LOG.info("received create-user topic for user with id ${msg.id}")
        userBean.create(msg.id, msg.email, msg.password, UserEntity.Role.USER)
        return msg
    }

    @Incoming("update-user")
    private fun receiveDeleteUser(msg: UpdateUserMessage): UpdateUserMessage {
        LOG.info("received update-user topic for user with id ${msg.id}")
        userBean.update(msg.id, msg.newEmail, msg.newPassword, null)
        return msg
    }

    @Incoming("delete-user")
    private fun receiveDeleteUser(msg: DeleteUserMessage): DeleteUserMessage {
        LOG.info("received delete-user topic for user with id ${msg.id}")
        userBean.delete(msg.id)
        return msg
    }
}
