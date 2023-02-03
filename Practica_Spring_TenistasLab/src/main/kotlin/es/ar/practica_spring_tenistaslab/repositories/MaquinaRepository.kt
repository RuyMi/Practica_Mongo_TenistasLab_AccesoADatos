package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MaquinaRepository:CoroutineCrudRepository<Maquina, ObjectId> {
    fun findByNumSerie(numSerie: String): Flow<Maquina?>


}