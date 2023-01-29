package repositories.producto

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Producto
import mu.KotlinLogging
import org.litote.kmongo.Id
import java.util.*

private val logger = KotlinLogging.logger {}

class ProductoRepositoryImpl: ProductoRepository {
    override fun findAll(): Flow<Producto> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Producto>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Producto>): Producto? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Producto>()
            .findOneById(id) ?: throw Exception("No existe el tenista con id $id")//TODO cambiar las excepciones
    }

    override suspend fun findByUUID(uuid: UUID): Producto? {
        TODO("Not yet implemented")
      // return MongoDbManager.database.getCollection<Producto>()
       //    .find().publisher? : throw Exception("No existe el tenista con id $uuid")
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