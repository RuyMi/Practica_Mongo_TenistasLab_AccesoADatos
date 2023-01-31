package repositories.usuario

import exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import models.Usuario
import models.UsuarioAPI
import mu.KotlinLogging
import services.ktorfit.KtorFitClient

private val logger = KotlinLogging.logger {}

class UsuarioRepositoryKtorfit {

    private val client by lazy { KtorFitClient.instance }

     suspend fun findAll(): Flow<UsuarioAPI> = withContext(Dispatchers.IO) {
        logger.debug { "findAll()" }
        val call = client.getAll()
        try {
            logger.debug { "findAll() - OK" }
            logger.debug { call }
            return@withContext call.asFlow()
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
            return call.data!!
        } catch (e: Exception) {
            logger.error { "findById(id=$id) - ERROR" }
            throw RestException("Error al obtener el usuario con id $id o no existe: ${e.message}")
        }
    }
}