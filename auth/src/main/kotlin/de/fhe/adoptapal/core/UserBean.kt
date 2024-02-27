package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.*
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger

@ApplicationScoped
class UserBean {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserBean::class.java)
    }

    @Inject
    private lateinit var repository: UserRepository

    @Transactional
    fun validateUserExists(id: Long): UserEntity {
        LOG.info("ensuring existence of user with id `$id`")
        return repository.find(id) ?: throw UserNotFoundException.byId(id)
    }

    @Transactional
    fun validateUserExists(email: String): UserEntity {
        LOG.info("ensuring existence of user with email `$email`")
        return repository.find(email) ?: throw UserNotFoundException.byEmail(email)
    }

    @Transactional
    fun validateEmailUnique(email: String) {
        LOG.info("ensuring uniqueness of email `$email`")
        if (repository.find(email) != null) {
            throw EmailTakenException(email)
        }
    }

    // TODO: ensure user can't update their own role

    @Transactional
    fun validateCredentials(credentials: UserCredentials, id: Long) {
        LOG.info("validating request credentials")
        val user = validateUserExists(id)

        LOG.info("validating email for user with id `$id`")
        if (credentials.email != user.email) {
            throw PasswordAuthenticationException()
        }

        LOG.info("validating password for user with id `$id`")
        if (!PasswordUtils.verifyPassword(credentials.password, user.password)) {
            throw PasswordAuthenticationException()
        }
    }

    @Transactional
    fun validateCredentials(jwt: JsonWebToken, id: Long) {
        LOG.info("validating user access")
        val user = validateUserExists(id)

        LOG.info("validating email for user with id `$id`")
        if (jwt.name != user.email) {
            throw TokenAuthenticationException()
        }
    }

    @Transactional
    fun create(request: CreateUser): UserEntity {
        LOG.info("creating user")
        validateEmailUnique(request.email)
        return repository.create(request.email, request.password, request.role)
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
    fun update(id: Long, request: UpdateUser) {
        LOG.info("updating user with id `$id`")
        repository.update(id, request.email, request.password, request.role)
    }

    @Transactional
    fun delete(id: Long) {
        LOG.info("deleting user with id `$id`")
        repository.delete(id)
    }
}
