package services.usuarios

import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import db.MongoDbManager
import models.Usuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
@Named("UsuarioService")
class UsuariosService {
    fun watch(): ChangeStreamPublisher<Usuario> {
        logger.debug { "watch()" }
        return MongoDbManager.database.getCollection<Usuario>()
            .watch<Usuario>()
            .publisher
    }
}