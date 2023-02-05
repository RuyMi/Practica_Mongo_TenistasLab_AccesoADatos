package es.ar.practica_spring_tenistaslab.repositories.KtorFitRepository

import es.ar.practica_spring_tenistaslab.models.Tarea

import es.ar.practica_spring_tenistaslab.exceptions.RestException
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import es.ar.practica_spring_tenistaslab.services.ktorfit.KtorFitClient

private val logger = KotlinLogging.logger {}
@Repository
class TareaRepositoryKtorfit {


    private val client by lazy { KtorFitClient.instance }

    suspend fun save(entity: Tarea): Tarea {
        logger.debug { "save(entity=$entity)" }
        try {
            val res = client.createTareas(entity)
            logger.debug { "save(entity=$entity) - OK" }
            return entity
        } catch (e: Exception) {
            logger.error { "save(entity=$entity) - ERROR" }
            throw RestException("Error al crear el usuario: ${e.message}")
        }

    }
}