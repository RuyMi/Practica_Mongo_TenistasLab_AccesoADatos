package es.ar.practica_spring_tenistaslab.repositories.RemoteCached


import es.ar.practica_spring_tenistaslab.models.Usuario
import es.ar.practica_spring_tenistaslab.models.toUsuario
import es.ar.practica_spring_tenistaslab.repositories.KtorFitRepository.UsuarioRepositoryKtorfit
import es.ar.practica_spring_tenistaslab.repositories.UsuariosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import es.ar.practica_spring_tenistaslab.services.ktorfit.KtorFitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*


private val logger = KotlinLogging.logger {}
private const val REFRESH_TIME = 6 * 10000L

@Repository
class UsuarioCachedRepositoryImpl
@Autowired constructor(
    private val usuarioRepository: UsuariosRepository,
    private val remote : UsuarioRepositoryKtorfit

    ) : UsuarioCachedRepository {




    suspend fun refresh() = withContext(Dispatchers.IO) {
        // Lanzamos una corutina para que se ejecute en segundo plano
        logger.debug { "RemoteCachedRepository.refresh()" }

        launch {
            do {
                logger.debug { "RemoteCachedRepository.refresh()" }
                val res = mutableListOf<Usuario>()
                remote.findAll().collect { res.add(it) }
                res.forEach { user ->
                    usuarioRepository.findByUuidUsuario(user.uuidUsuario).toList().firstOrNull()?.let {
                        usuarioRepository.save(it)
                    }
                }
                delay(REFRESH_TIME)
            } while (true)

        }
    }

    override suspend fun findAll(): Flow<Usuario> = withContext(Dispatchers.IO)  {
        logger.debug { "RemoteCachedRepository.getAll()" }
        logger.info { "Repositorio de raquetas findAll" }
        return@withContext usuarioRepository.findAll()

    }
    @Cacheable("usuario")
    override suspend fun findById(id: ObjectId): Usuario?= withContext(Dispatchers.IO) {
      return@withContext usuarioRepository.findById(id)
    }
    @Cacheable("usuario")
    override suspend fun findByUuid(uuid: String): Usuario?= withContext(Dispatchers.IO) {
        return@withContext usuarioRepository.findByUuidUsuario(uuid).toList().firstOrNull()
    }
    @CachePut("usuario")
    override suspend fun save(usuario: Usuario): Usuario = withContext(Dispatchers.IO) {
        val saved =
            usuario.copy(
                uuidUsuario = UUID.randomUUID().toString(),
                nombre=usuario.nombre,
                apellido=usuario.apellido,
                email=usuario.email,
                password=usuario.password,
                perfil=usuario.perfil,
                turno=usuario.turno,
            )

        return@withContext usuarioRepository.save(usuario)
    }


    @CacheEvict("usuario")
    override suspend fun delete(usuario: Usuario): Usuario? = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de usuario delete tenista: $usuario" }

        val usuariodb = usuarioRepository.findByUuidUsuario(usuario.uuidUsuario).toList().firstOrNull()
        usuariodb?.let {
            usuarioRepository.deleteById(it.id)
            return@withContext it
        }
        return@withContext null
    }

    @CacheEvict("usuario", allEntries = true)
   override suspend fun deleteAll() = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de Usuario deleteAll" }

        usuarioRepository.deleteAll()
    }


}