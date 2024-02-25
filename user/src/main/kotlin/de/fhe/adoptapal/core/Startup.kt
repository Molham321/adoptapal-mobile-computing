package de.fhe.adoptapal.core

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@ApplicationScoped
class Startup {

    @Inject
    lateinit var userBean: UserBean

    @Inject
    lateinit var addressBean: AddressBean

    companion object {
        // boss
        private const val USER_BOSS = "scott"
        private const val EMAIL_BOSS = "boss@example.com"
        private const val PASSWORD_BOSS = "secret"
        private const val PHONE_NUMBER_BOSS = "0123456789"

        private const val STREET_BOSS = "123 Main St"
        private const val CITY_BOSS = "Metropolis"
        private const val POSTAL_CODE_BOSS = "12345"

        // john
        private const val USER_JOHN = "john"
        private const val EMAIL_JOHN = "john@example.com"
        private const val PASSWORD_JOHN = "password"
        private const val PHONE_NUMBER_JOHN = ""

        private const val STREET_JOHN = "456 Oak St"
        private const val CITY_JOHN = "Smallville"
        private const val POSTAL_CODE_JOHN = "67890"

        // alice
        private const val USER_ALICE = "alice"
        private const val EMAIL_ALICE = "alice@example.com"
        private const val PASSWORD_ALICE = "password"
        private const val PHONE_NUMBER_ALICE = "9876543210"

        private const val STREET_ALICE = "789 Pine St"
        private const val CITY_ALICE = "Gotham"
        private const val POSTAL_CODE_ALICE = "54321"

        // bob
        private const val USER_BOB = "bob"
        private const val EMAIL_BOB = "bob@example.com"
        private const val PASSWORD_BOB = "pa55word"
        private const val PHONE_NUMBER_BOB = "5555555555"

        private const val STREET_BOB = "101 Elm St"
        private const val CITY_BOB = "Springfield"
        private const val POSTAL_CODE_BOB = "11111"
    }

    @Transactional
    fun loadUsersAndAddresses(@Observes evt: StartupEvent?) {
        userBean.deleteAll()
        addressBean.deleteAll()

        val addressBoss = addressBean.create(STREET_BOSS, CITY_BOSS, POSTAL_CODE_BOSS)
        userBean.create(USER_BOSS, EMAIL_BOSS, PASSWORD_BOSS, PHONE_NUMBER_BOSS, addressBoss.id)

        val addressJohn = addressBean.create(STREET_JOHN, CITY_JOHN, POSTAL_CODE_JOHN)
        userBean.create(USER_JOHN, EMAIL_JOHN, PASSWORD_JOHN, PHONE_NUMBER_JOHN, addressJohn.id)

        val addressAlice = addressBean.create(STREET_ALICE, CITY_ALICE, POSTAL_CODE_ALICE)
        userBean.create(USER_ALICE, EMAIL_ALICE, PASSWORD_ALICE, PHONE_NUMBER_ALICE, addressAlice.id)

        val addressBob = addressBean.create(STREET_BOB, CITY_BOB, POSTAL_CODE_BOB)
        userBean.create(USER_BOB, EMAIL_BOB, PASSWORD_BOB, PHONE_NUMBER_BOB, addressBob.id)
    }
}