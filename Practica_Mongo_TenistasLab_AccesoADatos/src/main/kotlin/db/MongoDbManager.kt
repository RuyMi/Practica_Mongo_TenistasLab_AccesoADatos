package db

import mu.KotlinLogging
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val logger = KotlinLogging.logger {}

object MongoDbManager {
    private lateinit var mongoClient: CoroutineClient
    lateinit var database: CoroutineDatabase

    // Para Mongo Atlas
    private const val MONGO_TYPE = "mongodb+srv://"
    private const val HOST = "cluster0.myhaiqw.mongodb.net"
    private const val PORT = 27017
    private const val DATABASE = "test"
    private const val USERNAME = "ar" //"mongoadmin"// "mongo"
    private const val PASSWORD = "tenistaslab"//"mongopass" //"xxxx"
    private const val OPTIONS = "?authSource=admin&retryWrites=true&w=majority"

    private const val MONGO_URI = "$MONGO_TYPE$USERNAME:$PASSWORD@$HOST/$DATABASE"

    init {
        logger.debug("Inicializando conexion a MongoDB")
        println("Inicializando conexion a MongoDB -> $MONGO_URI$OPTIONS")
        mongoClient =
            KMongo.createClient("$MONGO_URI$OPTIONS")
                .coroutine
        database = mongoClient.getDatabase("tenistas")
    }
}
