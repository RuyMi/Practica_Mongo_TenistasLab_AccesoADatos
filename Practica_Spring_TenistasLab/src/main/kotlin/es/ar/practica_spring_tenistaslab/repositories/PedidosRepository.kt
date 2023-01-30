package es.ar.practica_spring_tenistaslab.repositories

import es.ar.practica_spring_tenistaslab.models.Pedidos
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface PedidosRepository: CoroutineCrudRepository<Pedidos, ObjectId> {
    fun findByUuidPedidos(uuid: UUID):Pedidos?
}