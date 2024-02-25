package de.fhe.adoptapal.core

import de.fhe.adoptapal.messaging.KafkaProducer
import de.fhe.adoptapal.model.RequestSubject
import de.fhe.adoptapal.model.UserEntity
import de.fhe.adoptapal.repository.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.jboss.logging.Logger

@ApplicationScoped
class UserBean {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserBean::class.java)
    }

    @Inject
    private lateinit var repository: UserRepository

    @Inject
    lateinit var producer: KafkaProducer

    private fun validateUserExists(id: Long): UserEntity {
        LOG.info("ensuring existence of user with id `$id`")
        return repository.find(id) ?: throw UserNotFoundException.byId(id)
    }

    private fun validateUserExists(email: String): UserEntity {
        LOG.info("ensuring existence of user with email `$email`")
        return repository.find(email) ?: throw UserNotFoundException.byEmail(email)
    }

    private fun validateEmailUnique(email: String) {
        LOG.info("ensuring uniqueness of email `$email`")
        if (repository.find(email) != null) {
            throw EmailTakenException(email)
        }

        // TODO: ask auth service too
    }

    fun validateSubject(subject: RequestSubject) {
        LOG.info("validating request subject")
        validateUserExists(subject.id)

        // TODO: ask auth service
    }

    @Transactional
    fun create(username: String, email: String, password: String, phoneNumber: String, addressId: Long?): UserEntity {
        LOG.info("creating user")
        validateEmailUnique(email)
        val entity = repository.create(username, email, phoneNumber, addressId)
        producer.emitCreateUser(entity.id!!, email, password)
        return entity
    }

    @Transactional
    fun get(id: Long): UserEntity {
        LOG.info("getting user with id `$id`")
        return validateUserExists(id)
    }

    @Transactional
    fun get(email: String): UserEntity {
        LOG.info("getting user with email `$email`")
        return validateUserExists(email)
    }

    @Transactional
    fun getAll(): List<UserEntity> {
        LOG.info("getting all users")
        return repository.listAll()
    }

    @Transactional
    fun updateAuthorized(subject: RequestSubject, newUsername: String?, newEmail: String?, newPassword: String?, newPhoneNumber: String?) {
        validateSubject(subject)
        update(subject.id, newUsername, newEmail, newPassword, newPhoneNumber)
    }

    @Transactional
    fun update(id: Long, newUsername: String?, newEmail: String?, newPassword: String?, newPhoneNumber: String?) {
        LOG.info("updating user with id `$id`")
        if (newEmail == null || newPassword == null) {
            producer.emitUpdateUser(id, newEmail, newPassword)
        }
        repository.update(id, newUsername, newPhoneNumber)
    }

    @Transactional
    fun deleteAuthorized(subject: RequestSubject) {
        validateSubject(subject)
        delete(subject.id)
    }

    @Transactional
    fun delete(id: Long) {
        LOG.info("deleting user with id `$id`")
        repository.delete(id)
    }

    @Transactional
    fun deleteAll() {
        LOG.info("deleting all users")
        repository.deleteAll()
    }
}
