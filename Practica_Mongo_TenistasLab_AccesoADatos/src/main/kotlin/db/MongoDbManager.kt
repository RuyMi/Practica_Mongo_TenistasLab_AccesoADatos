package db

import mu.KotlinLogging
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import utils.PropertiesReader

val logger = KotlinLogging.logger {}
val properties = PropertiesReader("application.properties")

/**
 * Controlador de Entidades de Mongodb
 *
 *
 */

object MongoDbManager {
    private lateinit var mongoClient: CoroutineClient
    lateinit var database: CoroutineDatabase

    // Para Mongo Atlas
    private val MONGO_TYPE = properties.getProperty("MONGO_TYPE")
    private val HOST = properties.getProperty("HOST")
    private val PORT = properties.getProperty("PORT")
    private val DATABASE = properties.getProperty("DATABASE")
    private val USERNAME = properties.getProperty("USERNAME")
    private val PASSWORD = properties.getProperty("PASSWORD")
    private val OPTIONS = properties.getProperty("OPTIONS")

    private val MONGO_URI = "$MONGO_TYPE$USERNAME:$PASSWORD@$HOST/$DATABASE"

    init {
        logger.debug("Inicializando conexion a MongoDB")
        println("Inicializando conexion a MongoDB -> $MONGO_URI$OPTIONS")
        mongoClient =
            KMongo.createClient("$MONGO_URI$OPTIONS")
                .coroutine
        database = mongoClient.getDatabase("tenistas")
    }
}
