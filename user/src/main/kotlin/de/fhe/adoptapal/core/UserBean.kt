package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.*
import de.fhe.adoptapal.repository.UserRepository
import de.fhe.adoptapal.repository.AddressRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger

@ApplicationScoped
class UserBean {
    companion object {
        private val LOG: Logger = Logger.getLogger(UserBean::class.java)
    }

    @Inject
    private lateinit var repository: UserRepository

    @Inject
    private lateinit var addressRepository: AddressRepository

    @RestClient
    private lateinit var authService: AuthServiceClient

    fun validateUserExists(id: Long): UserEntity {
        LOG.info("ensuring existence of user with id `$id`")
        return repository.find(id) ?: throw UserNotFoundException.byId(id)
    }

    fun validateUserExists(email: String): UserEntity {
        LOG.info("ensuring existence of user with email `$email`")
        return repository.find(email) ?: throw UserNotFoundException.byEmail(email)
    }

    fun validateEmailUnique(email: String) {
        LOG.info("ensuring uniqueness of email `$email`")
        if (repository.find(email) != null) {
            throw EmailTakenException(email)
        }

        // TODO: ask auth service too
    }

    fun validateCredentials(credentials: UserCredentials, userId: Long) {
        LOG.info("validating request subject")
        validateUserExists(userId)
        authService.isCredentialsValid(userId, credentials.email, credentials.password)
    }

    @Transactional
    fun create(request: CreateUser): UserEntity {
        LOG.info("creating user")
        validateEmailUnique(request.email)
        val authUser = authService.create(request.email ,request.password)
        val address = addressRepository.create(request.address.street, request.address.city, request.address.postalCode)
        return repository.create(request.username, request.email, request.phoneNumber, address.id!!, authUser.id)
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
        if (request.email != null || request.password != null) {
            authService.update(user.authId!!, AuthUpdateUserRequest(request.email, request.password))
        }

        repository.update(id, request.username, request.phoneNumber)
        if (request.address != null) {
            addressRepository.update(user.addressId!!, request.address!!.street, request.address!!.city, request.address!!.postalCode)
        }
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
