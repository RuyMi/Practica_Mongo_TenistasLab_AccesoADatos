package repositories.producto

import db.MongoDbManager
import exceptions.ProductoException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Producto
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import java.util.*

private val logger = KotlinLogging.logger {}

@Single
@Named("ProductoRepositoryImpl")
class ProductoRepositoryImpl: ProductoRepository {
    override fun findAll(): Flow<Producto> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Producto>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Producto>): Producto? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Producto>()
            .findOneById(id) ?: throw ProductoException("No existe el producto con id $id")
    }

    override suspend fun findByUUID(uuid: String): Producto? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Producto>().findOne(Producto::uuidProducto eq uuid)?: throw ProductoException("No existe el producto con uuid $uuid")
    }

    override suspend fun save(entity: Producto): Producto? {
        logger.debug { "save($entity)" }
        return MongoDbManager.database.getCollection<Producto>()
            .save(entity).let { entity }
    }

    private suspend fun insert(entity: Producto): Producto {
        logger.debug { "save($entity) - creando" }
        return MongoDbManager.database.getCollection<Producto>()
            .save(entity).let { entity }
    }

    private suspend fun update(entity: Producto): Producto {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Producto>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Producto): Boolean {
        logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Producto>()
            .deleteOneById(entity.id).let { true }
    }
}