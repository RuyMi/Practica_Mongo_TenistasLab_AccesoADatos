package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Maquina
import es.ar.practica_spring_tenistaslab.models.Producto
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface ProductoRepository: CoroutineCrudRepository<Producto, ObjectId> {
    fun findByUuidProducto(uuid: String): Flow<Producto?>

}