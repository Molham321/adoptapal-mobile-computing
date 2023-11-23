package de.fhe.adoptapal

import com.fasterxml.jackson.annotation.JsonView
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class Views {
    open class Public
    class Private : Public()
}

@Entity
class UserEntity {
    // Getter und Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public::class)
    var id: Long? = null

    @JsonView(Views.Public::class)
    var username: String? = null
    @JsonView(Views.Public::class)
    var password: String? = null
}