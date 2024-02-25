package de.fhe.adoptapal.core

import de.fhe.adoptapal.model.*
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

@ApplicationScoped
class TokenBean {
    data class Token(var token: TokenEntity, var tokenString: String)

    companion object {
        const val VALIDITY_ID_KEY = "validityId"

        private val LOG: Logger = Logger.getLogger(TokenBean::class.java)

        @Throws(java.lang.Exception::class)
        private fun readPrivateKey(pemResName: String): PrivateKey {
            TokenBean::class.java.getResourceAsStream(pemResName).use { contentIS ->
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
    private lateinit var repository: TokenRepository

    @Inject
    private lateinit var userBean: UserBean

    private fun validateTokenExists(id: Long): TokenEntity {
        LOG.info("ensuring existence of token with id `$id`")
        return repository.find(id) ?: throw TokenNotFoundException(id)
    }

    private fun validateSubject(subject: RequestSubject) {
        userBean.validateSubject(subject)
    }

    private fun validateSubject(subject: RequestSubject, id: Long) {
        validateSubject(subject)

        val token = validateTokenExists(id)
        LOG.info("validating token access for token with id `$id` from user with id `${subject.id}`")
        if (id != token.userId) {
            // do not expose the existence of this token to another user
            throw TokenNotFoundException(id)
        }
    }

    fun createForUserAuthorized(subject: RequestSubject): Token {
        LOG.info("generating new token for user with id `${subject.id}`")
        validateSubject(subject)

        val currentTimeInSecs = TimeUtils.currentTime(TimeUtils.Unit.Seconds)
        val expiresAt = currentTimeInSecs + duration!!

        val newValidity = repository.create(subject.id, expiresAt)

        val privateKey = readPrivateKey(privateKeyLocation)
        val claimsBuilder = Jwt.claims()
        val groups = HashSet<String>()
        claimsBuilder.issuer(issuer)
        claimsBuilder.subject(subject.email)
        claimsBuilder.issuedAt(currentTimeInSecs)
        claimsBuilder.groups(groups)
        claimsBuilder.expiresAt(expiresAt)
        claimsBuilder.claim(VALIDITY_ID_KEY, newValidity.id)
        return Token(newValidity, claimsBuilder.jws().sign(privateKey))
    }

    fun getForUserAuthorized(subject: RequestSubject, id: Long): TokenEntity {
        validateSubject(subject, id)
        return getForUser(subject.id, id)
    }

    fun getAllForUserAuthorized(subject: RequestSubject): List<TokenEntity> {
        validateSubject(subject)
        return getAllForUser(subject.id)
    }

    fun getForUser(userId: Long, id: Long): TokenEntity {
        LOG.info("listing token with id `$id` for user with id `$userId`")
        deleteExpired()
        return validateTokenExists(id)
    }

    fun getAllForUser(userId: Long): List<TokenEntity> {
        LOG.info("listing tokens for user with id `$userId`")
        deleteExpired()
        return repository.listAllForUser(userId)
    }

    fun deleteForUserAuthorized(subject: RequestSubject, id: Long) {
        validateSubject(subject, id)
        deleteForUser(subject.id, id)
    }

    fun deleteAllForUserAuthorized(subject: RequestSubject) {
        validateSubject(subject)
        deleteAllForUser(subject.id)
    }

    fun deleteForUser(userId: Long, id: Long) {
        LOG.info("deleting token with id `$id` for user with id `$userId`")
        repository.deleteForUser(userId, id)
    }

    fun deleteAllForUser(userId: Long) {
        LOG.info("deleting all tokens for user with id `$userId`")
        repository.deleteAllForUser(userId)
    }

    fun delete(id: Long) {
        LOG.info("deleting token with id `$id`")
        repository.delete(id)
    }

    fun deleteAll() {
        LOG.info("deleting all tokens")
        repository.deleteAll()
    }

    fun deleteExpired() {
        LOG.info("deleting expired tokens")
        repository.deleteExpired()
    }

    fun validate(token: JsonWebToken) {
        LOG.info("validating token")
        val validity = token.claim<Any>(VALIDITY_ID_KEY)
        if (validity.isEmpty) {
            LOG.fatal("token did not contain validity claim")
            throw MissingTokenClaimException()
        }

        // BUG(erik): we cannot for some reason convert the internal number representation of the claim into a Long
        // directly, so unfortunately, to get forward, we have to go through a useless string conversion
        val v = validity.get().toString().toLong()
        if (repository.findById(v) == null) {
            throw TokenInvalidatedException()
        }
    }
}