package repositories.turno

import db.MongoDbManager
import exceptions.TurnoException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Producto
import models.Turno
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq

import java.util.*

private val logger = KotlinLogging.logger {}


@Single
@Named("TurnoRepositoryImpl")
class TurnoRepositoryImpl:TurnoRepository {
    override fun findAll(): Flow<Turno> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Turno>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Turno>): Turno? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Turno>()
            .findOneById(id) ?: throw TurnoException("No existe el Turno con id $id")
    }

    override suspend fun findByUUID(uuid: String): Turno? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Turno>().findOne(Turno::uuidTurno eq uuid)?: throw TurnoException("No existe el Turno con id $uuid")
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