package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.LocalDateTimeSerializer
import serializers.UUIDSerializer
import java.time.LocalDateTime
import java.util.*

/**
 *
 *
 * @property id
 * @property uuidTurno
 * @property fechaInicio
 * @property fechaFin
 */
@Serializable
data class Turno(
    @BsonId @Contextual
    val id : Id<Turno> = newId<Turno>(),
    val uuidTurno: String = UUID.randomUUID().toString(),
    @Serializable(LocalDateTimeSerializer::class)
    val fechaInicio: LocalDateTime,
    @Serializable(LocalDateTimeSerializer::class)
    val fechaFin: LocalDateTime
)



