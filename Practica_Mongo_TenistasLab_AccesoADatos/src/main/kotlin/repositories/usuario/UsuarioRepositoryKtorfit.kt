package repositories.usuario

import exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mapper.toUsuarioDto
import models.Usuario
import models.UsuarioAPI
import models.toUsuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.newId
import services.ktorfit.KtorFitClient

private val logger = KotlinLogging.logger {}


@Single
@Named("UsuarioRepositoryKtorfit")
class UsuarioRepositoryKtorfit {

    private val client by lazy { KtorFitClient.instance }

    suspend fun findAll(): Flow<Usuario> = withContext(Dispatchers.IO) {
        logger.debug { "findAll()" }
        val call = client.getAll()
        try {
            logger.debug { "findAll() - OK" }
            val res = mutableListOf<Usuario>()
            call.forEach {
                res.add(it.toUsuario())
            }
            return@withContext res.asFlow()
        } catch (e: Exception) {
            logger.error { "findAll() - ERROR" }
            throw RestException("Error al obtener los usuarios: ${e.message}")
        }

    }

    suspend fun findById(id: Int): Usuario? {
        logger.debug { "finById(id=$id)" }
        val call = client.getById(id)
        try {
            logger.debug { "findById(id=$id) - OK" }
            return call?.toUsuario()
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - ERROR" }
            throw RestException("Error al obtener el usuario con id $id o no existe: ${e.message}")
        }
    }


    suspend fun save(entity: Usuario): Usuario {
        logger.debug { "save(entity=$entity)" }
        try {
            val dto = entity.toUsuarioDto()
            val res = client.create(dto)
            logger.debug { "save(entity=$entity) - OK" }
            return entity
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - ERROR" }
            throw RestException("Error al crear el usuario: ${e.message}")
        }

    }

    suspend fun update(entity: Usuario): Usuario {
        logger.debug { "update(entity=$entity)" }
        try {
            val dto = entity.toUsuarioDto()
            val res = client.update(entity.id.toString(), dto)
            logger.debug { "update(entity=$entity) - OK" }
            return entity
        } catch (e: RestException) {
            logger.error { "update(entity=$entity) - ERROR" }
            throw RestException("Error al actualizar el usuario con ${entity.id}: ${e.message}")
        }
    }

    suspend fun delete(entity: Usuario): Usuario {
        logger.debug { "delete(entity=$entity)" }
        try {
            client.delete(entity.id.toString())
            logger.debug { "delete(entity=$entity) - OK" }
            return entity
        } catch (e: Exception) {
            logger.error { "delete(entity=$entity) - ERROR" }
            throw RestException("Error al eliminar el usuario con ${entity.id}: ${e.message}")
        }
    }



}