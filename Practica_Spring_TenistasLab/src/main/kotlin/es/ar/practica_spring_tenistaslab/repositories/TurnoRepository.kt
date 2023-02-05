package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import es.ar.practica_spring_tenistaslab.models.Turno
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*
@Repository
interface TurnoRepository: CoroutineCrudRepository<Turno, ObjectId> {
    fun findByUuidTurno(uuid: String): Flow<Turno>


}