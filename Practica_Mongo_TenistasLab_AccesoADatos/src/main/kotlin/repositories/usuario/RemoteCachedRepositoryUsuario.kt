package repositories.usuario

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mapper.toUserModel
import models.Usuario
import models.toUsuario
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import services.ktorfit.KtorFitClient
import services.sqldelight.SqlDeLightClient

private val logger = KotlinLogging.logger {}
private const val REFRESH_TIME = 6 * 10000L // 60 seconds, el tiempo que tarda en refrescar


@Single
@Named("RemoteCachedRepositoryUsuario")
class RemoteCachedRepositoryUsuario() {
        // Inyectar dependencias
        private val remote = KtorFitClient.instance
        private val client = SqlDeLightClient
        private val cached = client.queries


        suspend fun refresh() = withContext(Dispatchers.IO) {
            // Lanzamos una corutina para que se ejecute en segundo plano
            logger.debug { "RemoteCachedRepository.refresh()" }
            launch {
                do {
                    logger.debug { "RemoteCachedRepository.refresh()" }
                    cached.removeAllUsers()
                    val res = mutableListOf<Usuario>()
                    remote.getAll().forEach { res.add(it.toUsuario()) }

                    res.forEach { user ->
                        cached.insertUser(user.id.toString(),user.uuidUsuario, user.nombre, user.apellido, user.email, user.password.toString(),
                            user.perfil.toString(),user.turno.toString(),user.pedido.toString())
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

        suspend fun findById(id: Long): Usuario? {
            logger.debug { "RemoteCachedRepository.findById(id=$id)" }
            return cached.selectById(id.toString()).executeAsOne().toUserModel()
        }

        suspend fun findByUuid(uuid: String): Usuario? {
        logger.debug { "RemoteCachedRepository.findByUuid(uuid=$uuid)" }
        return cached.selectByUuid(uuid).executeAsOne().toUserModel()
    }

        suspend fun save(entity: Usuario): Usuario {
            logger.debug { "RemoteCachedRepository.save(entity=$entity)" }
            val dto = remote.create(entity)
            cached.insertUser(dto.id.toString(), dto.uuidUsuario, dto.nombre, dto.apellido, dto.email, dto.password.toString(), dto.perfil.name, dto.turno.toString(), dto.pedido.toString())
            return cached.selectLastUser().executeAsOne().toUserModel()
        }

        suspend fun update(entity: Usuario): Usuario {
            // actualizamos localmente y remotamente
            logger.debug { "RemoteCachedRepository.update(entity=$entity)" }
            cached.update(
                id = entity.id.toString(),
                uuidUsuario = entity.uuidUsuario,
                nombre = entity.nombre,
                apellido = entity.apellido,
                email = entity.email,
                password = entity.password.toString(),
                perfil = entity.perfil.name,
                turno = entity.turno.toString(),
                pedido = entity.pedido.toString()
            )
            val dto = remote.update(entity.id.toString().toLong(), entity)
            return dto.toUsuario()
        }


        suspend fun delete(entity: Usuario): Boolean {
            // borramos localmente y remotamente
            logger.debug { "RemoteCachedRepository.delete(entity=$entity)" }
            cached.delete(entity.id.toString())
            remote.delete(entity.id.toString().toLong())
            return true
        }
}
