package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.enums.TipoEstado
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.LocalDateSerializer
import serializers.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class Pedidos(
    @BsonId @Contextual
    val id: Id<Usuario> = newId<Usuario>(),
    @Serializable(UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    val estado: TipoEstado,
    @Serializable(LocalDateSerializer::class)
    val fechaEntrada: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val fechaSalidaProgramada: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val fechaEntrega: LocalDate?,
    val precio: Double,
    @Contextual
    val usuario: Id<Usuario>
) {

}
