package de.fhe.ai.mc.resources

class AuthRequest() {
    lateinit var username: String
    lateinit var password: String
}

class AuthResponse(var token: String)

class Message(var content: String)
