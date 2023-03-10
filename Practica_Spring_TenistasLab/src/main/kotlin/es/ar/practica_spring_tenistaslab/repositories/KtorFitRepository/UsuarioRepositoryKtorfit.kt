package es.ar.practica_spring_tenistaslab.repositories.KtorFitRepository

import es.ar.practica_spring_tenistaslab.models.Usuario
import es.ar.practica_spring_tenistaslab.models.UsuarioAPI
import es.ar.practica_spring_tenistaslab.exceptions.RestException
import es.ar.practica_spring_tenistaslab.models.toUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import es.ar.practica_spring_tenistaslab.services.ktorfit.KtorFitClient



private val logger = KotlinLogging.logger {}
@Repository
class UsuarioRepositoryKtorfit {

    private val client by lazy { KtorFitClient.instance }

     suspend fun findAll(): Flow<Usuario> = withContext(Dispatchers.IO) {
        logger.debug { "findAll()" }
        val call = client.getAll()
        try {
            logger.debug { "findAll() - OK" }
            logger.debug { call }
            val res = call.map { it.toUsuario() }
            return@withContext res.asFlow()
        } catch (e: Exception) {
            logger.error { "findAll() - ERROR" }
            throw RestException("Error al obtener los usuarios: ${e.message}")
        }

    }

    suspend fun findById(id: Int): UsuarioAPI? {
        logger.debug { "finById(id=$id)" }
        val call = client.getById(id)
        try {
            logger.debug { "findById(id=$id) - OK" }
            return call
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - ERROR" }
            throw RestException("Error al obtener el usuario con id $id o no existe: ${e.message}")
        }
    }




   suspend fun save(entity: Usuario): Usuario {
        logger.debug { "save(entity=$entity)" }
        try {
            val res = client.create(entity)
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
            val res = client.update(entity.id.toString().toLong(), entity)
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
            client.delete(entity.id.toString().toLong())
            logger.debug { "delete(entity=$entity) - OK" }
            return entity
        } catch (e: Exception) {
            logger.error { "delete(entity=$entity) - ERROR" }
            throw RestException("Error al eliminar el usuario con ${entity.id}: ${e.message}")
        }
    }



}