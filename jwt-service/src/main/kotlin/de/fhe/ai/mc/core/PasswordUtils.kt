package de.fhe.ai.mc.core

import io.quarkus.elytron.security.common.BcryptUtil
import org.wildfly.security.password.Password
import org.wildfly.security.password.PasswordFactory
import org.wildfly.security.password.interfaces.BCryptPassword
import org.wildfly.security.password.util.ModularCrypt

/*
    Helper Class to encrypt and verify user passwords
 */
class PasswordUtils {
    companion object {

        fun hashPassword(plainPassword: String): String =
                BcryptUtil.bcryptHash(plainPassword)

        @Throws(java.lang.Exception::class)
        fun verifyPassword(plainPassword: String, encryptedPassword: String): Boolean {
            val rawPassword: Password = ModularCrypt.decode(encryptedPassword)
            val factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT)
            val restored = factory.translate(rawPassword) as BCryptPassword
            return factory.verify(restored, plainPassword.toCharArray())
        }
    }
}
