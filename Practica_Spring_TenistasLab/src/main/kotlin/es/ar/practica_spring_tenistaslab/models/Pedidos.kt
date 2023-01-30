package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.enums.TipoEstado
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import serializers.LocalDateSerializer
import serializers.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Document("pedidos")
data class Pedidos(
    @BsonId @Contextual
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidPedidos: UUID = UUID.randomUUID(),
    val estado: TipoEstado,
    @Serializable(LocalDateSerializer::class)
    val fechaEntrada: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val fechaSalidaProgramada: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val fechaEntrega: LocalDate?,
    val precio: Double,

    val usuario: Usuario,
) {

}