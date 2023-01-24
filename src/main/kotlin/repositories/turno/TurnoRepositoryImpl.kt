package repositories.turno

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Producto
import models.Turno
import mu.KotlinLogging
import org.litote.kmongo.Id
import org.litote.kmongo.eq

import java.util.*

private val logger = KotlinLogging.logger {}

class TurnoRepositoryImpl:TurnoRepository {
    override fun findAll(): Flow<Turno> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Turno>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Turno>): Turno? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Turno>()
            .findOneById(id) ?: throw Exception("No existe el Turno con id $id")//TODO cambiar las excepciones
    }

    override suspend fun findByUUID(uuid: UUID): Turno? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Turno>().findOne(Turno::uuid eq uuid)
    }

    override suspend fun save(entity: Turno): Turno? {
        logger.debug { "save($entity)" }
        return MongoDbManager.database.getCollection<Turno>()
            .save(entity).let { entity }
    }


    private suspend fun insert(entity: Turno): Turno {
        logger.debug { "save($entity) - creando" }
        return MongoDbManager.database.getCollection<Turno>()
            .save(entity).let { entity }
    }

    private suspend fun update(entity: Turno): Turno {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Turno>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Turno): Boolean {
        logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Turno>()
            .deleteOneById(entity.id).let { true }
    }
}