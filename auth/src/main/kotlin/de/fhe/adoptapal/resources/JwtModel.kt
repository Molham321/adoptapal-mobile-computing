package de.fhe.adoptapal.resources

// TODO(erik): this should be id based

class AuthRequest {
    lateinit var username: String
    lateinit var password: String
}

data class LoginResponse(var token: String, var duration: Long)
