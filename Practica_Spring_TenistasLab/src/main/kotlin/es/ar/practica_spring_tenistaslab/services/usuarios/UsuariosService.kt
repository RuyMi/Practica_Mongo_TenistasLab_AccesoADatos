package es.ar.practica_spring_tenistaslab.services.usuarios

import es.ar.practica_spring_tenistaslab.models.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ChangeStreamEvent
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class UsuariosService
@Autowired constructor(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) {
    fun watch(): Flow<ChangeStreamEvent<Usuario>> {
        logger.info("watch()")
        return reactiveMongoTemplate.changeStream(
            "usuario",
            ChangeStreamOptions.empty(),
            Usuario::class.java
        ).asFlow()
    }
}