package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

/**
 * Entity class representing an address in the system.
 *
 * @property id The unique identifier for the address.
 * @property street The street of the address.
 * @property city The city of the address.
 * @property postalCode The postal code of the address.
 * @property createdAt The timestamp indicating when the address was created.
 */
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