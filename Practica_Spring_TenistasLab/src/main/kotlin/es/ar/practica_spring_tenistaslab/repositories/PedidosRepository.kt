package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Pedidos
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface PedidosRepository: CoroutineCrudRepository<Pedidos, ObjectId> {
    fun findByUuidPedidos(uuid: UUID):Pedidos?
}