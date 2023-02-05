package repositories.maquina

import db.MongoDbManager
import exceptions.MaquinaException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Maquina
import models.Pedidos
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.eq

import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * Maquina repository impl
 *
 * @constructor Create empty Maquina repository impl
 */
@Single
@Named("MaquinaRepositoryImpl")
class MaquinaRepositoryImpl:MaquinaRepository {


    override fun findAll(): Flow<Maquina> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Maquina>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Maquina>): Maquina? {
       logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Maquina>()
            .findOneById(id) ?: throw MaquinaException("No existe el maquina con id $id")
    }

    override suspend fun findByUUID(uuid: String): Maquina? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Maquina>().findOne(Maquina::numSerie eq uuid) ?: throw MaquinaException("No existe el maquina con uuid $uuid")
    }

    override suspend fun save(entity: Maquina): Maquina? {
       logger.debug { "save($entity)" }
        return MongoDbManager.database.getCollection<Maquina>()
            .save(entity).let { entity }
    }
    private suspend fun insert(entity: Maquina): Maquina {
       logger.debug { "save($entity) - creando" }
        return MongoDbManager.database.getCollection<Maquina>()
            .save(entity).let { entity }
    }

    private suspend fun update(entity: Maquina): Maquina {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Maquina>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Maquina): Boolean {
       logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Maquina>()
            .deleteOneById(entity.id).let { true }
    }
}