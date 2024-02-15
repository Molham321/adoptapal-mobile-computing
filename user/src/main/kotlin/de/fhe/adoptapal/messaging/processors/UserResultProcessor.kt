package de.fhe.adoptapal.messaging.processors

import de.fhe.adoptapal.messaging.models.KafkaUserRegistrationResponse
import de.fhe.adoptapal.model.UserEntity
import de.fhe.adoptapal.repository.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.control.ActivateRequestContext
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.xml.bind.ValidationException
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.util.logging.Logger

@ApplicationScoped
class UserResultProcessor {

    @Inject
    private lateinit var entityManager: EntityManager

    @Inject
    private lateinit var userRepository: UserRepository

    @Channel("user-creation-request")
    private lateinit var createdUserEmitter: Emitter<UserEntity>

    @Channel("user-update-request")
    private lateinit var updatedUserEmitter: Emitter<UserEntity>

    @Channel("user-registration-ack-request")
    private lateinit var registrationUserAckEmitter: Emitter<KafkaUserRegistrationResponse>

    @Incoming("user-registration-response")
    @ActivateRequestContext
    fun processUserCreation(user: UserEntity) {
        val logger = Logger.getLogger(UserResultProcessor::class.java.name)
        logger.info("[ADD] Received $user...")

        try {
            userRepository.createUser(user)
            registrationUserAckEmitter.send(KafkaUserRegistrationResponse(id = user.id, successful = true))
        } catch (e: ValidationException) {
            logger.severe("Validation error during user creation: $e")
            registrationUserAckEmitter.send(KafkaUserRegistrationResponse(id = user.id, successful = false))
        } catch (e: Exception) {
            logger.severe("Error processing user creation: $e")
            registrationUserAckEmitter.send(KafkaUserRegistrationResponse(id = user.id, successful = false))
        }
    }

    @Incoming("user-deletion-response")
    @ActivateRequestContext
    fun processUserDeletion(userId: Long) {
        val logger = Logger.getLogger(UserResultProcessor::class.java.name)
        logger.info("[DELETE] Received $userId")

        try {
            val user = entityManager.find(UserEntity::class.java, userId)
            entityManager.remove(user)
            updatedUserEmitter.send(user)
        } catch (e: Exception) {
            logger.severe("Error processing user deletion: $e")
        }
    }

}
