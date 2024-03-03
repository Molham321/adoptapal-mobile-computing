package de.fhe.adoptapal.model

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * Entity class representing a user in the system.
 *
 * @property id The unique identifier for the user.
 * @property username The username of the user.
 * @property phoneNumber The phone number associated with the user.
 * @property address The [AddressEntity] representing the user's address.
 * @property authId The authentication ID associated with the user.
 * @property createdAt The timestamp indicating when the user was created.
 */
@Entity
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var username: String
    lateinit var phoneNumber: String

    @OneToOne
    lateinit var address: AddressEntity
    var authId: Long? = null

    lateinit var createdAt: LocalDateTime
}
