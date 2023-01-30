package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.models.enums.TipoMaquina
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import serializers.LocalDateSerializer
import serializers.UUIDSerializer
import java.time.LocalDate
import java.util.*
@Document("Maquina")
data class Maquina(
    @BsonId @Contextual
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val numSerie: UUID = UUID.randomUUID(),
    val marca: String,
    val modelo: String,
    @Serializable(LocalDateSerializer::class)
    val fechaAdquisicion: LocalDate,
    val descripcion: String,
    val tipo: TipoMaquina
) {
}