package de.fhe.adoptapal.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * Entity class representing a token.
 */
@Entity
class TokenEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var userId: Long? = null

    var expiresAt: Long = 0
}
