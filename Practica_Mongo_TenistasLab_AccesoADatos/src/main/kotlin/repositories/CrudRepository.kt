package repositories

import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Crud repository
 *
 */
interface CrudRepository<T, ID> {
    /**
     * Find all
     *
     * @return
     */
    fun findAll(): Flow<T>

    /**
     * Find by id
     *
     * @param id
     * @return
     */
    suspend fun findById(id: ID): T?

    /**
     * Find by u u i d
     *
     * @param uuid
     * @return
     */
    suspend fun findByUUID(uuid: String): T?

    /**
     * Save
     *
     * @param entity
     * @return
     */
    suspend fun save(entity: T): T?

    /**
     * Delete
     *
     * @param entity
     * @return
     */
    suspend fun delete(entity: T): Boolean
}