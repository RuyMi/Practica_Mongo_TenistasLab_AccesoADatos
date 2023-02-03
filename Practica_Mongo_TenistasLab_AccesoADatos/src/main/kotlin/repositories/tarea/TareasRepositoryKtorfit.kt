package repositories.tarea

import exceptions.RestException
import mapper.toTareaDto
import models.Tarea
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import services.ktorfit.KtorFitClient


private val logger = KotlinLogging.logger {}

@Single
@Named("TareasRepositoryKtorfit")
class TareasRepositoryKtorfit {

    private val client by lazy { KtorFitClient.instance }

    suspend fun save(entity: Tarea): Tarea {
        logger.debug { "save(entity=$entity)" }
        try {
            val tarea = entity.toTareaDto()
            val res = client.createTareas(tarea)
            logger.debug { "save(entity=$entity) - OK" }
            return entity
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - ERROR" }
            throw RestException("Error al crear el usuario: ${e.message}")
        }

    }
}