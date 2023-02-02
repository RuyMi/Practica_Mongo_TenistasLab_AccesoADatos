package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.models.enums.TipoMaquina

import kotlinx.serialization.Serializable

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import serializers.LocalDateSerializer
import serializers.UUIDSerializer
import java.time.LocalDate
import java.util.*
@Document("Maquina")
data class Maquina(
    @Id
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val numSerie:String = UUID.randomUUID().toString(),
    val marca: String,
    val modelo: String,
    @Serializable(LocalDateSerializer::class)
    val fechaAdquisicion: LocalDate,
    val descripcion: String,
    val tipo: TipoMaquina
) {
}