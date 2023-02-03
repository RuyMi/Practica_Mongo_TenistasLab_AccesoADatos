package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import es.ar.practica_spring_tenistaslab.serializers.LocalDateTimeSerializer
import es.ar.practica_spring_tenistaslab.serializers.UUIDSerializer
import java.time.LocalDateTime
import java.util.*


@Document("turno")
data class Turno(
    @Id
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidTurno: String = UUID.randomUUID().toString(),
    @Serializable(LocalDateTimeSerializer::class)
    val fechaInicio: LocalDateTime,
    @Serializable(LocalDateTimeSerializer::class)
    val fechaFin: LocalDateTime
) {

}