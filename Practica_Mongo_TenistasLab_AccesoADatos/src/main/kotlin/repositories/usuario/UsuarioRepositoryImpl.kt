package repositories.usuario

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import models.Usuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import services.sqldelight.SqlDeLightClient
import java.util.*

private val logger = KotlinLogging.logger {}

@Single
@Named("UsuarioRepositoryImpl")
class UsuarioRepositoryImpl:UsuarioRepository {

    val cache = SqlDeLightClient.queries
    override fun findAll(): Flow<Usuario> {
        logger.debug { "findAll($)" }
        return MongoDbManager.database.getCollection<Usuario>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Usuario>): Usuario? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Usuario>()
            .findOneById(id) ?: throw Exception("No existe el Usuario con id $id")
    }

    override suspend fun findByUUID(uuid: String): Usuario? {
        logger.debug { "findByUUID($uuid)" }
        return MongoDbManager.database.getCollection<Usuario>().findOne(Usuario::uuidUsuario eq uuid)
    }

    override suspend fun save(entity: Usuario): Usuario? {
        logger.debug { "save($entity)" }
        cache.insertUser(
            entity.id.toString(),
            entity.uuidUsuario,
            entity.nombre,
            entity.apellido,
            entity.email,
            entity.password.toString(),
            entity.perfil.name,
            entity.turno.toString(),
            entity.pedido.toString())
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