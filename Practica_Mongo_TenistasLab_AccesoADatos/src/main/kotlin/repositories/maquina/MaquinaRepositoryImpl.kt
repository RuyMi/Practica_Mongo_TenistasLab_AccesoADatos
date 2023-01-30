package repositories.maquina

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Maquina
import mu.KotlinLogging
import org.litote.kmongo.Id
import org.litote.kmongo.MongoOperator

import java.util.*

private val logger = KotlinLogging.logger {}

class MaquinaRepositoryImpl:MaquinaRepository {

    override fun findAll(): Flow<Maquina> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Maquina>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Maquina>): Maquina? {
       logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Maquina>()
            .findOneById(id) ?: throw Exception("No existe el maquina con id $id")//TODO cambiar las excepciones
    }

    override suspend fun findByUUID(uuid: UUID): Maquina? {
        TODO("Not yet implemented")
       // logger.debug { "findByUUID($uuid)" }
      //  return MongoDbManager.database.getCollection<Producto>().findOne(Maquina::num)
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
            .deleteOneById(MongoOperator.id).let { true }
    }
}