package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.CreateAddress
import de.fhe.adoptapal.model.CreateUser
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@ApplicationScoped
class Startup {

    @Inject
    lateinit var userBean: UserBean

    companion object {
        private val scott = CreateUser()
        private val john = CreateUser()
        private val alice = CreateUser()
        private val bob = CreateUser()

        init {
            scott.username = "scott"
            scott.email = "boss@example.com"
            scott.password = "secret"
            scott.phoneNumber = "0123456789"
            scott.address = CreateAddress()
            scott.address.street = "123 Main St"
            scott.address.city = "Metropolis"
            scott.address.postalCode = "12345"

            john.username = "john"
            john.email = "john@example.com"
            john.password = "password"
            john.phoneNumber = ""
            john.address = CreateAddress()
            john.address.street = "456 Oak St"
            john.address.city = "Smallville"
            john.address.postalCode = "67890"

            alice.username = "alice"
            alice.email = "alice@example.com"
            alice.password = "password"
            alice.phoneNumber = "9876543210"
            alice.address = CreateAddress()
            alice.address.street = "789 Pine St"
            alice.address.city = "Gotham"
            alice.address.postalCode = "54321"

            bob.username = "alice"
            bob.email = "bob@example.com"
            bob.password = "pa55word"
            bob.phoneNumber = "5555555555"
            bob.address = CreateAddress()
            bob.address.street = "101 Elm St"
            bob.address.city = "Springfield"
            bob.address.postalCode = "11111"
        }
    }

    @Transactional
    fun loadUsersAndAddresses(@Observes evt: StartupEvent?) {
        userBean.deleteAll()

        userBean.create(scott)
        userBean.create(john)
        userBean.create(alice)
        userBean.create(bob)
    }
}