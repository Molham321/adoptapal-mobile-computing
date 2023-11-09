package de.fhe.ai.mc

class User() {
    lateinit var username: String
    lateinit var password: String
    lateinit var role: String

    constructor(username: String, password: String, role: String):this() {
        this.username = username
        this.password = password
        this.role = role
    }

    override fun toString(): String {
        return "User(username=$username, password=$password, role=$role)"
    }
}