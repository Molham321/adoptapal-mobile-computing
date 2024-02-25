package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.AddressEntity
import de.fhe.adoptapal.repository.AddressRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.jboss.logging.Logger

@ApplicationScoped
class AddressBean {
    companion object {
        private val LOG: Logger = Logger.getLogger(AddressBean::class.java)
    }

    @Inject
    private lateinit var repository: AddressRepository

    private fun validateAddressExists(id: Long): AddressEntity {
        LOG.info("ensuring existence of address with id `$id`")
        return repository.find(id) ?: throw AddressNotFoundException(id)
    }

    @Transactional
    fun create(street: String, city: String, postalCode: String): AddressEntity {
        LOG.info("creating address")
        return repository.create(street, city, postalCode)
    }

    @Transactional
    fun get(id: Long): AddressEntity {
        LOG.info("getting address with id `$id`")
        return validateAddressExists(id)
    }

    @Transactional
    fun getAll(): List<AddressEntity> {
        LOG.info("getting all addresses")
        return repository.listAll()
    }

    @Transactional
    fun update(id: Long, newStreet: String?, newCity: String?, newPostalCode: String?) {
        LOG.info("updating address with id `$id`")
        repository.update(id, newStreet, newCity, newPostalCode)
    }

    @Transactional
    fun delete(id: Long) {
        LOG.info("deleting address with id `$id`")
        repository.delete(id)
    }

    @Transactional
    fun deleteAll() {
        LOG.info("deleting all users")
        repository.deleteAll()
    }
}
