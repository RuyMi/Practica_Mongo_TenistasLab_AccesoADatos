package repositories

import kotlinx.coroutines.flow.Flow
import java.util.*

interface CrudRepository<T, ID> {
    fun findAll(): Flow<T>
    suspend fun findById(id: ID): T?
    suspend fun findByUUID(uuid: String): T?
    suspend fun save(entity: T): T?
    suspend fun delete(entity: T): Boolean
}