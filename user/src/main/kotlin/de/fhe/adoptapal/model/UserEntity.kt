package de.fhe.adoptapal.model

import jakarta.persistence.*
import java.time.LocalDateTime

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
