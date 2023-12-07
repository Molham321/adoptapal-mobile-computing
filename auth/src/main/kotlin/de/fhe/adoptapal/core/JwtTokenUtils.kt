package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.UserEntity
import io.smallrye.jwt.build.Jwt
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import kotlin.collections.HashSet

// TODO(erik): make this a bean which injects the jwt stuff

/**
 * Helper class to create valid JWT Tokens
 */
class JwtTokenUtils {
    companion object {
        @Throws(java.lang.Exception::class)
        fun generateToken(userEntity: UserEntity, duration: Long, issuer: String): String {
            val privateKeyLocation = "/privateTestKey.pem"
            val privateKey = readPrivateKey(privateKeyLocation)
            val claimsBuilder = Jwt.claims()
            val currentTimeInSecs = currentTimeInSecs().toLong()
            val groups = HashSet<String>()
            claimsBuilder.issuer(issuer)
            claimsBuilder.subject(userEntity.username)
            claimsBuilder.issuedAt(currentTimeInSecs)
            claimsBuilder.groups(groups)
            claimsBuilder.expiresAt(currentTimeInSecs + duration)
            return claimsBuilder.jws().sign(privateKey)
        }

        @Throws(java.lang.Exception::class)
        private fun readPrivateKey(pemResName: String): PrivateKey {
            JwtTokenUtils::class.java.getResourceAsStream(pemResName).use { contentIS ->
                val tmp = ByteArray(4096)
                val length: Int = contentIS!!.read(tmp)
                return decodePrivateKey(String(tmp, 0, length, Charset.defaultCharset()))
            }
        }

        @Throws(java.lang.Exception::class)
        private fun decodePrivateKey(pemEncoded: String): PrivateKey {
            val encodedBytes = toEncodedBytes(pemEncoded)
            val keySpec = PKCS8EncodedKeySpec(encodedBytes)
            val kf = KeyFactory.getInstance("RSA")
            return kf.generatePrivate(keySpec)
        }

        @Throws(java.lang.Exception::class)
        private fun toEncodedBytes(pemEncoded: String): ByteArray {
            val normalizedPem = removeBeginEnd(pemEncoded)
            return Base64.getDecoder().decode(normalizedPem)
        }

        @Throws(java.lang.Exception::class)
        private fun removeBeginEnd(pemString: String): String {
            var pem = pemString
            pem = pem.replace("-----BEGIN (.*)-----".toRegex(), "")
            pem = pem.replace("-----END (.*)----".toRegex(), "")
            pem = pem.replace("\r\n".toRegex(), "")
            pem = pem.replace("\n".toRegex(), "")
            return pem.trim { it <= ' ' }
        }

        private fun currentTimeInSecs(): Int {
            val currentTimeMS = System.currentTimeMillis()
            return (currentTimeMS / 1000).toInt()
        }
    }
}
