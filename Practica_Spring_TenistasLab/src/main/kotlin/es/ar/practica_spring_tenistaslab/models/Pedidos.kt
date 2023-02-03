package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.dto.UsuarioDTO
import kotlinx.serialization.Serializable
import models.enums.TipoEstado
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import es.ar.practica_spring_tenistaslab.serializers.LocalDateSerializer
import es.ar.practica_spring_tenistaslab.serializers.ObjectIdSerializer
import es.ar.practica_spring_tenistaslab.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import java.time.LocalDate
import java.util.*

@Serializable
@Document("pedidos")
data class Pedidos(
    @Id @Serializable(ObjectIdSerializer::class)
    val id: ObjectId = ObjectId.get(),
    val uuidPedidos: String = UUID.randomUUID().toString(),
    val estado: TipoEstado,
    @Serializable(LocalDateSerializer::class)
    val fechaEntrada: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val fechaSalidaProgramada: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val fechaEntrega: LocalDate?,
    val precio: Double,
    val usuario: UsuarioDTO,
) {

}