package repositories.usuario

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Producto
import models.Usuario
import mu.KotlinLogging
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import java.util.*

private val logger = KotlinLogging.logger {}

class UsuarioRepositoryImpl:UsuarioRepository {
    override fun findAll(): Flow<Usuario> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Usuario>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Usuario>): Usuario? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Usuario>()
            .findOneById(id) ?: throw Exception("No existe el Usuario con id $id")//TODO cambiar las excepciones
    }

    override suspend fun findByUUID(uuid: UUID): Usuario? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Usuario>().findOne(Usuario::uuid eq uuid)
    }

    override suspend fun save(entity: Usuario): Usuario? {
        logger.debug { "save($entity)" }
        return MongoDbManager.database.getCollection<Usuario>()
            .save(entity).let { entity }
    }

    private suspend fun insert(entity: Usuario): Usuario {
        logger.debug { "save($entity) - creando" }
        return MongoDbManager.database.getCollection<Usuario>()
            .save(entity).let { entity }
    }

    private suspend fun update(entity: Usuario): Usuario {
        logger.debug { "save($entity) - actualizando" }
        return MongoDbManager.database.getCollection<Usuario>()
            .save(entity).let { entity }
    }

    override suspend fun delete(entity: Usuario): Boolean {
        logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Usuario>()
            .deleteOneById(entity.id).let { true }
    }
}