package de.fhe.ai.mc.resources

data class MyObject(val name: String)

data class ValueWithIpAddress(var value: String, var ipAddress: String) {
    override fun toString() = "$value from $ipAddress"
}