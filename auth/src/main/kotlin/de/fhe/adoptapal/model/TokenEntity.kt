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

    var expiresAt: Long = 0

    var invalidated: Boolean = false
}

@ApplicationScoped
class TokenRepository: PanacheRepository<TokenEntity> {
    @Transactional
    fun create(expiresAt: Long): TokenEntity {
        val tokenEntity = TokenEntity()
        tokenEntity.expiresAt = expiresAt

        deleteExpired()
        persist(tokenEntity)
        flush()
        return tokenEntity
    }

    @Transactional
    fun invalidateById(id: Long) {
        deleteExpired()
        update("invalidated = true where id = :id", Parameters().and("id", id))
    }

    @Transactional
    fun deleteExpired() {
        val now = TimeUtils.currentTime(TimeUtils.Unit.Seconds)
        delete("expiresAt <= :now", Parameters().and("now", now))
    }
}