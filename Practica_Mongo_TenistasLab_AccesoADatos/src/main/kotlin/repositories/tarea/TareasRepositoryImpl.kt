package repositories.tarea

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Tarea
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq

import java.util.*

private val logger = KotlinLogging.logger {}

@Single
@Named("TareasRepositoryImpl")
class TareasRepositoryImpl:TareasRepository {
    override fun findAll(): Flow<Tarea> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Tarea>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Tarea>): Tarea? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Tarea>()
            .findOneById(id) ?: throw Exception("No existe el Tarea con id $id")//TODO cambiar las excepciones
    }

    override suspend fun findByUUID(uuid: String): Tarea? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Tarea>().findOne(Tarea::uuidTarea eq uuid)
    }

    override suspend fun save(entity: Tarea): Tarea? {
       logger.debug { "save($entity)" }
        return MongoDbManager.database.getCollection<Tarea>()
            .save(entity).let { entity }
    }

    private suspend fun insert(entity: Tarea): Tarea {
        logger.debug { "save($entity) - creando" }
        return MongoDbManager.database.getCollection<Tarea>()
            .save(entity).let { entity }
    }

    private suspend fun update(entity: Tarea): Tarea {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Tarea>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Tarea): Boolean {
       logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Tarea>()
            .deleteOneById(entity.id).let { true }
    }
}