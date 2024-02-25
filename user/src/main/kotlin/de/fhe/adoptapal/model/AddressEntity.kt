package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class AddressEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var street: String
    lateinit var city: String
    lateinit var postalCode: String

    lateinit var createdAt: LocalDateTime
}