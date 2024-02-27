package de.fhe.adoptapal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(unique = true)
    lateinit var email: String

    lateinit var username: String
    lateinit var phoneNumber: String

    var addressId: Long? = null
    var authId: Long? = null

    lateinit var createdAt: LocalDateTime
}
