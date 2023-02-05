package es.ar.practica_spring_tenistaslab.repositories.RemoteCached

import es.ar.practica_spring_tenistaslab.models.Usuario
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId

interface UsuarioCachedRepository {
    suspend fun findAll(): Flow<Usuario>

    suspend fun findById(id: ObjectId): Usuario?
    suspend fun findByUuid(uuid: String): Usuario?

    suspend fun save(usuario: Usuario): Usuario

    suspend fun delete(usuario: Usuario): Usuario?




    suspend fun deleteAll()
}