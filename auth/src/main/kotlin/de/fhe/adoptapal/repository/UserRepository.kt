package de.fhe.adoptapal.repository

import de.fhe.adoptapal.core.PasswordUtils
import de.fhe.adoptapal.model.UserEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

/**
 * Repository class for managing user entities.
 */
@ApplicationScoped
class UserRepository : PanacheRepository<UserEntity> {

    /**
     * Creates a new user entity with the specified email, password, and role.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @param role The role of the user (default is USER).
     * @return The created user entity.
     */
    @Transactional
    fun create(email: String, password: String, role: UserEntity.Role = UserEntity.Role.USER): UserEntity {
        val entity = UserEntity()
        entity.email = email
        entity.password = PasswordUtils.hashPassword(password)
        entity.role = role.name

        persist(entity)
        flush()
        return entity
    }

    /**
     * Finds a user entity by its ID.
     *
     * @param id The ID of the user.
     * @return The user entity if found, otherwise null.
     */
    @Transactional
    fun find(id: Long): UserEntity? = find("id", id).firstResult()

    /**
     * Finds a user entity by its email.
     *
     * @param email The email of the user.
     * @return The user entity if found, otherwise null.
     */
    @Transactional
    fun find(email: String): UserEntity? = find("email", email).firstResult()

    /**
     * Updates the specified fields of a user entity.
     *
     * @param id The ID of the user.
     * @param newEmail The new email (if not null).
     * @param newPassword The new password (if not null).
     * @param newRole The new role (if not null).
     */
    @Transactional
    fun update(id: Long, newEmail: String?, newPassword: String?, newRole: UserEntity.Role?) {
        var fields = ""
        val params = Parameters.with("id", id)

        newEmail?.let {
            fields += ", email = :email"
            params.and("email", it)
        }
        newPassword?.let {
            fields += ", password = :password"
            params.and("password", PasswordUtils.hashPassword(it))
        }
        newRole?.let {
            fields += ", role = :role"
            params.and("role", it.name)
        }

        fields = fields.substring(2)
        update("$fields where id = :id", params)
    }

    /**
     * Deletes a user entity by its ID.
     *
     * @param id The ID of the user to be deleted.
     */
    @Transactional
    fun delete(id: Long) = deleteById(id)
}
