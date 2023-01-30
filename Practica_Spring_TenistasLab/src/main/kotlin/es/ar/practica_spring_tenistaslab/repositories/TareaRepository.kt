package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import es.ar.practica_spring_tenistaslab.models.Tarea
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface TareaRepository: CoroutineCrudRepository<Tarea, ObjectId> {
    fun findByUuidTarea(uuid: UUID):Tarea?
}