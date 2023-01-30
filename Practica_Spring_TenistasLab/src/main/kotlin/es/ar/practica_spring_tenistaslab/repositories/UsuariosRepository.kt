package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import es.ar.practica_spring_tenistaslab.models.Usuario
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UsuariosRepository: CoroutineCrudRepository<Usuario, ObjectId> {
    fun findByUuidUsuario(uuid:UUID):Usuario
}