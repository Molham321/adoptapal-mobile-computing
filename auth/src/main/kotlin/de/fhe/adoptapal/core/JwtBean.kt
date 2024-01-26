package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.TokenRepository
import de.fhe.adoptapal.model.UserEntity
import de.fhe.adoptapal.resources.AuthResource
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

/**
 * Helper class to create valid JWT Tokens
 */
@ApplicationScoped
class JwtBean {

    class ValidationException(message: String): Exception(message) {
        companion object {
            fun missingClaim(): ValidationException = ValidationException("a token did not contain the validity claim '$VALIDITY_ID_KEY'")
            fun missingEntity(id: Long): ValidationException = ValidationException("a token validity entity with id '$id' was not found")
        }
    }

    companion object {
        const val VALIDITY_ID_KEY = "validityId"

        private val LOG: Logger = Logger.getLogger(AuthResource::class.java)

        @Throws(java.lang.Exception::class)
        private fun readPrivateKey(pemResName: String): PrivateKey {
            JwtBean::class.java.getResourceAsStream(pemResName).use { contentIS ->
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
    }

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private lateinit var issuer: String

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.publickey.location")
    private lateinit var publicKeyLocation: String

    @Inject
    @ConfigProperty(name = "de.fhe.adoptapal.privatekey.location")
    private lateinit var privateKeyLocation: String

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.token.age")
    private var duration: Long? = null

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Throws(java.lang.Exception::class)
    fun generateToken(userEntity: UserEntity): Pair<String, Long> {
        val currentTimeInSecs = TimeUtils.currentTime(TimeUtils.Unit.Seconds)
        val expiresAt = currentTimeInSecs + duration!!

        val newValidity = tokenRepository.create(expiresAt)

        val privateKey = readPrivateKey(privateKeyLocation)
        val claimsBuilder = Jwt.claims()
        val groups = HashSet<String>()
        claimsBuilder.issuer(issuer)
        claimsBuilder.subject(userEntity.email)
        claimsBuilder.issuedAt(currentTimeInSecs)
        claimsBuilder.groups(groups)
        claimsBuilder.expiresAt(expiresAt)
        claimsBuilder.claim(VALIDITY_ID_KEY, newValidity.id)
        return claimsBuilder.jws().sign(privateKey) to expiresAt
    }

    @Throws(ValidationException::class)
    fun validate(token: JsonWebToken): Boolean {
        val validity = token.claim<Any>(VALIDITY_ID_KEY)
        if (validity.isEmpty) {
            LOG.fatal("token did not contain validity claim")
            throw ValidationException.missingClaim()
        }

        // BUG(erik): we cannot for some reason convert the internal number representation of the claim into a Long
        // directly, so unfortunately, to get forward, we have to go through a useless string conversion
        val v = validity.get().toString().toLong()

        val tokenEntity = tokenRepository.findById(v)

        if (tokenEntity == null) {
            LOG.error("token validity id was not found")
            throw ValidationException.missingEntity(v)
        }

        return !tokenEntity.invalidated
    }

    @Throws(ValidationException::class)
    fun invalidate(token: JsonWebToken) {
        val validity = token.claim<Any>(VALIDITY_ID_KEY)

        if (validity.isEmpty) {
            LOG.fatal("token did not contain validity claim")
            throw ValidationException.missingClaim()
        }

        // NOTE(erik): see above
        val v = validity.get().toString().toLong()
        tokenRepository.invalidateById(v)
    }
}
