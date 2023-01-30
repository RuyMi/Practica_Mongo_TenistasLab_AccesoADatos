package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import serializers.LocalDateTimeSerializer
import serializers.UUIDSerializer
import java.time.LocalDateTime
import java.util.*


@Document("turno")
data class Turno(
    @Id
    val id : ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidTurno: UUID = UUID.randomUUID(),
    @Serializable(LocalDateTimeSerializer::class)
    val fechaInicio: LocalDateTime,
    @Serializable(LocalDateTimeSerializer::class)
    val fechaFin: LocalDateTime
) {

}