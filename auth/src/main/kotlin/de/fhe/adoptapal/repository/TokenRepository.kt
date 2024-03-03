package de.fhe.adoptapal.repository

import de.fhe.adoptapal.core.TimeUtils
import de.fhe.adoptapal.model.TokenEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

/**
 * Repository class for managing token entities.
 */
@ApplicationScoped
class TokenRepository : PanacheRepository<TokenEntity> {

    /**
     * Creates a new token entity for the specified user and expiration time.
     *
     * @param userId The ID of the user associated with the token.
     * @param expiresAt The expiration time of the token.
     * @return The created token entity.
     */
    @Transactional
    fun create(userId: Long, expiresAt: Long): TokenEntity {
        val tokenEntity = TokenEntity()
        tokenEntity.userId = userId
        tokenEntity.expiresAt = expiresAt

        persist(tokenEntity)
        flush()
        return tokenEntity
    }

    /**
     * Finds a token entity by its ID.
     *
     * @param id The ID of the token.
     * @return The token entity if found, otherwise null.
     */
    @Transactional
    fun find(id: Long) = find("id", id).firstResult()

    /**
     * Lists all token entities associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of token entities for the specified user.
     */
    @Transactional
    fun listAllForUser(userId: Long) = list("where userId = :userId", Parameters().and("userId", userId))

    /**
     * Deletes a token entity by its ID.
     *
     * @param id The ID of the token to be deleted.
     */
    @Transactional
    fun delete(id: Long) = deleteById(id)

    /**
     * Deletes a specific token entity associated with a user.
     *
     * @param userId The ID of the user.
     * @param id The ID of the token to be deleted.
     */
    @Transactional
    fun deleteForUser(userId: Long, id: Long) =
        delete("where id = :id and userId = :userId", Parameters().and("userId", userId).and("id", id))

    /**
     * Deletes all token entities associated with a specific user.
     *
     * @param userId The ID of the user.
     */
    @Transactional
    fun deleteAllForUser(userId: Long) = delete("where userId = :userId", Parameters().and("userId", userId))

    /**
     * Deletes all expired token entities.
     */
    @Transactional
    fun deleteExpired() =
        delete("expiresAt <= :now", Parameters().and("now", TimeUtils.currentTime(TimeUtils.Unit.Seconds)))
}