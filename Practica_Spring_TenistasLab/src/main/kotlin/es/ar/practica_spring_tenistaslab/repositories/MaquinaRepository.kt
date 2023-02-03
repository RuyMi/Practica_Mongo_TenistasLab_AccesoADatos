package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MaquinaRepository:CoroutineCrudRepository<Maquina, ObjectId> {
    fun findMaquinaByNumSerie(numSerie: String):Maquina?

}