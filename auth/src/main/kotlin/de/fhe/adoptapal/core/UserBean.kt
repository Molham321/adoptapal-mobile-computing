package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.*
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger

/**
 * Class representing the business logic for user-related operations.
 */
@ApplicationScoped
class UserBean {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserBean::class.java)
    }

    @Inject
    private lateinit var repository: UserRepository

    @Inject
    private lateinit var tokenBean: TokenBean

    /**
     * Ensures the existence of a user with the given ID.
     * @param id The ID of the user.
     * @return The user entity.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Transactional
    fun validateUserExists(id: Long): UserEntity {
        LOG.info("ensuring existence of user with id `$id`")
        return repository.find(id) ?: throw UserNotFoundException.byId(id)
    }

    /**
     * Ensures the existence of a user with the given email.
     * @param email The email of the user.
     * @return The user entity.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Transactional
    fun validateUserExists(email: String): UserEntity {
        LOG.info("ensuring existence of user with email `$email`")
        return repository.find(email) ?: throw UserNotFoundException.byEmail(email)
    }

    /**
     * Validates that the email is unique.
     * @param email The email to be validated.
     * @throws EmailTakenException if the email is already taken.
     */
    @Transactional
    fun validateEmailUnique(email: String) {
        LOG.info("ensuring uniqueness of email `$email`")
        if (repository.find(email) != null) {
            throw EmailTakenException(email)
        }
    }

    /**
     * Validates user credentials for authentication.
     * @param credentials The user credentials.
     * @param id The ID of the user.
     * @throws PasswordAuthenticationException if authentication fails.
     */
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

    /**
     * Validates user credentials from a JSON Web Token (JWT).
     * @param jwt The JSON Web Token.
     * @param id The ID of the user.
     * @throws TokenAuthenticationException if authentication fails.
     */
    @Transactional
    fun validateCredentials(jwt: JsonWebToken, id: Long) {
        LOG.info("validating user access")
        val user = validateUserExists(id)

        LOG.info("validating email for user with id `$id`")
        if (jwt.name != user.email) {
            throw TokenAuthenticationException()
        }
    }

    /**
     * Creates a new user.
     * @param request The request containing user information.
     * @return The created user entity.
     * @throws EmailTakenException if the email is already taken.
     */
    @Transactional
    fun create(request: CreateUser): UserEntity {
        LOG.info("creating user")
        validateEmailUnique(request.email)
        return repository.create(request.email, request.password, UserEntity.Role.USER)
    }

    /**
     * Retrieves user information by ID.
     * @param id The ID of the user.
     * @return The user entity.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Transactional
    fun get(id: Long): UserEntity {
        LOG.info("getting user with id `$id`")
        return validateUserExists(id)
    }

    /**
     * Retrieves user information by email.
     * @param email The email of the user.
     * @return The user entity.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Transactional
    fun get(email: String): UserEntity {
        LOG.info("getting user with email `$email`")
        return validateUserExists(email)
    }

    /**
     * Updates user information.
     * @param id The ID of the user.
     * @param request The request containing updated user information.
     */
    @Transactional
    fun update(id: Long, request: UpdateUser) {
        LOG.info("updating user with id `$id`")
        tokenBean.deleteAllForUser(id)
        repository.update(id, request.email, request.password, null)
    }

    /**
     * Deletes a user.
     * @param id The ID of the user to be deleted.
     */
    @Transactional
    fun delete(id: Long) {
        LOG.info("deleting user with id `$id`")
        repository.delete(id)
    }
}
