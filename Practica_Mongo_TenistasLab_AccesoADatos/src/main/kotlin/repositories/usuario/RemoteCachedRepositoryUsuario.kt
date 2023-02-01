package repositories.usuario

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Usuario
import mu.KotlinLogging
import services.ktorfit.KtorFitClient
import services.sqldelight.SqlDeLightClient

private val logger = KotlinLogging.logger {}
private const val REFRESH_TIME = 6 * 10000L // 60 seconds, el tiempo que tarda en refrescar

class RemoteCachedRepositoryUsuario(client: SqlDeLightClient) {


        // Inyectar dependencias
        private val remote = KtorFitClient.instance
        private val cached = client.queries


        suspend fun refresh() = withContext(Dispatchers.IO) {
            // Lanzamos una corutina para que se ejecute en segundo plano
            logger.debug { "RemoteCachedRepository.refresh()" }
            launch {
                do {
                    logger.debug { "RemoteCachedRepository.refresh()" }
                    cached.removeAllUsers()
                    remote.getAll().forEach { user ->
                        cached.insertUser(user.id.toLong(),user.uuidUsuario, user.nombre, user.apellido, user.email, user.password,user.perfil,user.turno,user.pedido)
                        //mappearlo
                    }
                    delay(REFRESH_TIME)
                } while (true)

            }
        }

        fun findAll(): Flow<List<Usuario>> {
            logger.debug { "RemoteCachedRepository.getAll()" }
            return cached.selectUsers().asFlow().mapToList()
                .map { it.map { user -> user.toUserModel() } }
        }

        fun findById(id: Long): database.Usuario {
            logger.debug { "RemoteCachedRepository.findById(id=$id)" }
            // consulamos la base de datos local
            return cached.selectById(id).executeAsOne()
        }

        suspend fun save(entity: Usuario): Usuario {
            // Insertamos remotamente y localmente (mirar el orden por la id, si es uuid lo hacemos antes localmente)
            // si dependemos de una base de datos remota se lo debemos pedir a la base de datos remota
            logger.debug { "RemoteCachedRepository.save(entity=$entity)" }
            val dto = remote.create(entity)
            val user = Usuario(
                id = dto.id,
                first_name = dto.first_name!!,
                last_name = dto.last_name!!,
                avatar = dto.avatar,
                email = dto.email
            )
            cached.insertUser(user.id.toLong(), user.first_name, user.last_name, user.email, user.avatar)
            // Devolvemos pero con la id que nos ha devuelto el servidor
            //return cached.selectLastUser().executeAsOne().toUserModel()
            return user
        }

        suspend fun update(entity: Usuario): Usuario {
            // actualizamos localmente y remotamente
            logger.debug { "RemoteCachedRepository.update(entity=$entity)" }
            cached.update(
                id = entity.id,
                first_name = entity.first_name,
                last_name = entity.last_name,
                email = entity.email,
                avatar = entity.avatar
            )
            val dto = remote.update(entity.id, entity)
            return User(
                id = dto.id,
                first_name = dto.first_name,
                last_name = dto.last_name,
                avatar = dto.avatar,
                email = dto.email
            )
        }


        suspend fun delete(entity: Usuario): Usuario {
            // borramos localmente y remotamente
            logger.debug { "RemoteCachedRepository.delete(entity=$entity)" }
            cached.delete(entity.id)
            remote.delete(entity.id)
            return entity
        }
    }
}