package services.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.AppDatabase
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object SqlDeLightClient {
    private val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    init {
        // Creamos la base de datos
        logger.debug { "SqlDeLightClient.init() - Create Schemas" }
        AppDatabase.Schema.create(driver)
    }

    val queries = AppDatabase(driver).appDatabaseQueries

    // limpiamos las tablas
    fun removeAllData() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        queries.transaction {
            logger.debug { "SqlDeLightClient.removeAllData() - users " }
            queries.removeAllUsers()
        }
    }


}