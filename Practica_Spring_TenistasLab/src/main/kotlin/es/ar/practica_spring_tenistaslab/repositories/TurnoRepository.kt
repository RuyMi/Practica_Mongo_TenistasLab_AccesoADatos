package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import es.ar.practica_spring_tenistaslab.models.Turno
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface TurnoRepository: CoroutineCrudRepository<Turno, ObjectId> {
    fun findByUuidTurno(uuid: UUID):Turno?

}