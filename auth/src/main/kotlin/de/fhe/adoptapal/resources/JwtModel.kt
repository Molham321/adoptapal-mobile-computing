package de.fhe.adoptapal.resources

class AuthRequest {
    lateinit var email: String
    lateinit var password: String
}

data class LoginResponse(var token: String, var duration: Long)
