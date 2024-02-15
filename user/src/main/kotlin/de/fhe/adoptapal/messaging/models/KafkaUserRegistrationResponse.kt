package de.fhe.adoptapal.messaging.models

class KafkaUserRegistrationResponse(val id: Long?, val successful: Boolean) {
    override fun toString() : String =
        "UserResponse(id=$id, successful = $successful)"
}
