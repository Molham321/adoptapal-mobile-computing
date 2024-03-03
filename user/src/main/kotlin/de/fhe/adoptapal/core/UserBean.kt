package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.*
import de.fhe.adoptapal.repository.UserRepository
import de.fhe.adoptapal.repository.AddressRepository
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import java.net.URI

/**
 * Business logic class for user-related operations.
 *
 * @property repository The [UserRepository] for accessing and manipulating user data.
 * @property addressRepository The [AddressRepository] for accessing and manipulating address data.
 * @property authService The authentication service client for validating user credentials.
 */
@ApplicationScoped
class UserBean {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserBean::class.java)
    }

    @Inject
    private lateinit var repository: UserRepository

    @Inject
    private lateinit var addressRepository: AddressRepository

    private var authService: AuthServiceClient = QuarkusRestClientBuilder.newBuilder()
        .baseUri(URI.create("http://auth:8080/"))
        .build(AuthServiceClient::class.java)

    /**
     * Validates the existence of a user with the given ID.
     *
     * @param id The ID of the user to validate.
     * @return The [UserEntity] corresponding to the given ID.
     * @throws UserNotFoundException if the user with the given ID is not found.
     */
    fun validateUserExists(id: Long): UserEntity {
        LOG.info("ensuring existence of user with id `$id`")
        return repository.find(id) ?: throw UserNotFoundException.byId(id)
    }

    /**
     * Validates user credentials using the [AuthServiceClient].
     *
     * @param credentials The [UserCredentials] containing user email and password.
     * @param userId The ID of the user for whom credentials are validated.
     * @throws PasswordAuthenticationException if the password authentication fails.
     */
    fun validateCredentials(credentials: UserCredentials, userId: Long) {
        LOG.info("validating request subject")
        validateUserExists(userId)
        try {
            authService.isCredentialsValid(userId, credentials.email, credentials.password)
        } catch (e: Exception) {
            throw PasswordAuthenticationException()
        }
    }

    /**
     * Validates user credentials using a token and the [AuthServiceClient].
     *
     * @param token The authentication token.
     * @param userId The ID of the user for whom credentials are validated.
     * @throws TokenAuthenticationException if the token authentication fails.
     */
    fun validateCredentials(token: String, userId: Long) {
        LOG.info("validating request subject")
        validateUserExists(userId)
        try {
            authService.isTokenValid(userId, token)
        } catch (e: Exception) {
            throw TokenAuthenticationException()
        }
    }

    /**
     * Creates a new user with the provided information.
     *
     * @param request The [CreateUser] request containing user details.
     * @return The created [UserEntity].
     */
    @Transactional
    fun create(request: CreateUser): UserEntity {
        LOG.info("creating user")
        val authUser = authService.create(AuthCreateUserRequest(request.email, request.password))
        val address = addressRepository.create(request.address.street, request.address.city, request.address.postalCode)
        return repository.create(request.username, request.phoneNumber, address, authUser.id)
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The [UserEntity] corresponding to the given ID.
     */
    @Transactional
    fun get(id: Long): UserEntity {
        LOG.info("getting user with id `$id`")
        return validateUserExists(id)
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all [UserEntity] objects.
     */
    @Transactional
    fun getAll(): List<UserEntity> {
        LOG.info("getting all users")
        return repository.listAll()
    }

    /**
     * Updates an existing user with the provided information.
     *
     * @param id The ID of the user to update.
     * @param request The [UpdateUser] request containing updated user details.
     */
    @Transactional
    fun update(id: Long, request: UpdateUser) {
        LOG.info("updating user with id `$id`")
        val user = validateUserExists(id)

        repository.update(id, request.username, request.phoneNumber)
        if (request.address != null) {
            addressRepository.update(
                user.address.id!!,
                request.address!!.street,
                request.address!!.city,
                request.address!!.postalCode
            )
        }
    }

    /**
     * Deletes a user by ID along with associated address and authentication details.
     *
     * @param credentials The [UserCredentials] for authentication.
     * @param id The ID of the user to delete.
     */
    @Transactional
    fun delete(credentials: UserCredentials, id: Long) {
        LOG.info("deleting user with id `$id`")
        val user = validateUserExists(id)
        addressRepository.delete(user.address.id!!)
        authService.delete(id, credentials.email, credentials.password)
        repository.delete(id)
    }
}
