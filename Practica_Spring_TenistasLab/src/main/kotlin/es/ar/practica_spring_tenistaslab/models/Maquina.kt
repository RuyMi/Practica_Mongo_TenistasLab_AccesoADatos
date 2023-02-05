package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.models.enums.TipoMaquina

import kotlinx.serialization.Serializable

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
@Document("Maquina")
data class Maquina(
    @Id @Serializable(ObjectIdSerializer::class)
    val id: ObjectId = ObjectId.get(),
    val numSerie:String = UUID.randomUUID().toString(),
    val marca: String,
    val modelo: String,
    @Serializable(LocalDateSerializer::class)
    val fechaAdquisicion: LocalDate,
    val descripcion: String,
    val tipo: TipoMaquina
) {
}