package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.AddressRepository
import de.fhe.adoptapal.model.UserRepository
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@ApplicationScoped
class Startup {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var addressRepository: AddressRepository

    companion object {
        private const val USER_SCOTT = "scott"
        private const val EMAIL_BOSS = "boss@example.com"
        private const val PHONE_NUMBER_BOSS = "0123456789"
        private const val USER_JDOE = "jdoe"
        private const val EMAIL_USER = "user@example.com"
        private const val STREET_BOSS = "123 Main St"
        private const val CITY_BOSS = "Metropolis"
        private const val POSTAL_CODE_BOSS = "12345"
        private const val STREET_USER = "456 Oak St"
        private const val CITY_USER = "Smallville"
        private const val POSTAL_CODE_USER = "67890"

        private const val USER_ALICE = "alice"
        private const val EMAIL_ALICE = "alice@example.com"
        private const val PHONE_NUMBER_ALICE = "9876543210"
        private const val USER_BOB = "bob"
        private const val EMAIL_BOB = "bob@example.com"
        private const val PHONE_NUMBER_BOB = "5555555555"
        private const val STREET_ALICE = "789 Pine St"
        private const val CITY_ALICE = "Gotham"
        private const val POSTAL_CODE_ALICE = "54321"
        private const val STREET_BOB = "101 Elm St"
        private const val CITY_BOB = "Springfield"
        private const val POSTAL_CODE_BOB = "11111"
    }

    @Transactional
    fun loadUsersAndAddresses(@Observes evt: StartupEvent?) {
        userRepository.let {
            it.deleteAll()

            val addressBoss = addressRepository.add(STREET_BOSS, CITY_BOSS, POSTAL_CODE_BOSS)
            it.add(USER_SCOTT, EMAIL_BOSS, addressBoss.id, PHONE_NUMBER_BOSS)

            val addressUser = addressRepository.add(STREET_USER, CITY_USER, POSTAL_CODE_USER)
            it.add(USER_JDOE, EMAIL_USER, addressUser.id, null)

            val addressAlice = addressRepository.add(STREET_ALICE, CITY_ALICE, POSTAL_CODE_ALICE)
            it.add(USER_ALICE, EMAIL_ALICE, addressAlice.id, PHONE_NUMBER_ALICE)

            val addressBob = addressRepository.add(STREET_BOB, CITY_BOB, POSTAL_CODE_BOB)
            it.add(USER_BOB, EMAIL_BOB, addressBob.id, PHONE_NUMBER_BOB)

        }
    }
}