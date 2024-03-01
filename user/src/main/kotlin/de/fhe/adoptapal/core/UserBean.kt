package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.*
import de.fhe.adoptapal.repository.UserRepository
import de.fhe.adoptapal.repository.AddressRepository
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import java.net.URI

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

    fun validateUserExists(id: Long): UserEntity {
        LOG.info("ensuring existence of user with id `$id`")
        return repository.find(id) ?: throw UserNotFoundException.byId(id)
    }

    fun validateUserExists(email: String): UserEntity {
        LOG.info("ensuring existence of user with email `$email`")
        return repository.find(email) ?: throw UserNotFoundException.byEmail(email)
    }

    fun validateCredentials(credentials: UserCredentials, userId: Long) {
        LOG.info("validating request subject")
        validateUserExists(userId)
        try {
            authService.isCredentialsValid(userId, credentials.email, credentials.password)
        } catch (e: Exception) {
            throw PasswordAuthenticationException()
        }
    }

    fun validateCredentials(token: String, userId: Long) {
        LOG.info("validating request subject")
        validateUserExists(userId)
        try {
            authService.isTokenValid(userId, token)
        } catch (e: Exception) {
            throw TokenAuthenticationException()
        }
    }

    @Transactional
    fun create(request: CreateUser): UserEntity {
        LOG.info("creating user")
        val authUser = authService.create(AuthCreateUserRequest(request.email, request.password))
        val address = addressRepository.create(request.address.street, request.address.city, request.address.postalCode)
        return repository.create(request.username, request.phoneNumber, address, authUser.id)
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
    fun update(id: Long, request: UpdateUser) {
        LOG.info("updating user with id `$id`")
        val user = validateUserExists(id)

        repository.update(id, request.username, request.phoneNumber)
        if (request.address != null) {
            addressRepository.update(user.address.id!!, request.address!!.street, request.address!!.city, request.address!!.postalCode)
        }
    }

    @Transactional
    fun delete(credentials: UserCredentials, id: Long) {
        LOG.info("deleting user with id `$id`")
        authService.delete(id, credentials.email, credentials.password)
        repository.delete(id)
    }
}
