package repositories.producto

import kotlinx.coroutines.flow.Flow
import models.Producto
import org.litote.kmongo.Id
import java.util.*

class ProductoRepositoryImpl: ProductoRepository {
    override fun findAll(): Flow<Producto> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: Id<Producto>): Producto? {
        TODO("Not yet implemented")
    }

    override suspend fun findByUUID(uuid: UUID): Producto? {
        TODO("Not yet implemented")
    }

    override suspend fun save(entity: Producto): Producto? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: Producto): Boolean {
        TODO("Not yet implemented")
    }
}