package de.fhe.adoptapal.model

import de.fhe.adoptapal.core.TimeUtils
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.transaction.Transactional

@Entity
class TokenEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    var userId: Long? = null

    var expiresAt: Long = 0
}

@ApplicationScoped
class TokenRepository: PanacheRepository<TokenEntity> {
    @Transactional
    fun create(userId: Long, expiresAt: Long): TokenEntity {
        val tokenEntity = TokenEntity()
        tokenEntity.userId = userId
        tokenEntity.expiresAt = expiresAt

        deleteExpired()
        persist(tokenEntity)
        flush()
        return tokenEntity
    }

    @Transactional
    fun listAllForUser(userId: Long): List<TokenEntity> {
        return list("where userId = :userId", Parameters().and("userId", userId))
    }

    @Transactional
    fun deleteExpired() {
        val now = TimeUtils.currentTime(TimeUtils.Unit.Seconds)
        delete("expiresAt <= :now", Parameters().and("now", now))
    }

    @Transactional
    fun delete(id: Long) {
        deleteById(id)
    }

    @Transactional
    fun deleteAllForUser(userId: Long) {
        delete("where userId = :userId", Parameters().and("userId", userId))
    }
}