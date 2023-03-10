package repositories.pedidos

import db.MongoDbManager
import exceptions.PedidoException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Pedidos
import models.Producto
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import java.util.*

private val logger = KotlinLogging.logger {}


@Single
@Named("PedidosRepositoryImpl")
class PedidosRepositoryImpl:PedidosRepository {
    override fun findAll(): Flow<Pedidos> {
       logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Pedidos>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Pedidos>): Pedidos? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Pedidos>()
            .findOneById(id) ?: throw PedidoException("No existe el Pedidos con id $id")
    }

    override suspend fun findByUUID(uuid: String): Pedidos? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Pedidos>().findOne(Pedidos::uuidPedidos eq uuid) ?: throw PedidoException("No existe el Pedidos con uuid $uuid")
    }

    override suspend fun save(entity: Pedidos): Pedidos? {
        logger.debug { "save($entity)" }
        return MongoDbManager.database.getCollection<Pedidos>()
            .save(entity).let { entity }
    }

    private suspend fun insert(entity: Pedidos): Pedidos {
       logger.debug { "save($entity) - creando" }
        return MongoDbManager.database.getCollection<Pedidos>()
            .save(entity).let { entity }
    }

    private suspend fun update(entity: Producto): Producto {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Producto>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Pedidos): Boolean {
        logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Pedidos>()
            .deleteOneById(entity.id).let { true }
    }
}