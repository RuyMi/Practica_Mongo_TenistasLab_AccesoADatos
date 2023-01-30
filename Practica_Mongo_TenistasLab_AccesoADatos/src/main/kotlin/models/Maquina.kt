package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.enums.TipoMaquina
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.LocalDateSerializer
import serializers.UUIDSerializer
import java.time.LocalDate
import java.util.*

/**
 * Maquina
 *
 * @property id
 * @property numSerie
 * @property marca
 * @property modelo
 * @property fechaAdquisicion
 * @property swingweight
 * @property balance
 * @property rigidez
 */
@Serializable
data class Maquina(
        @BsonId @Contextual
        val id: Id<Maquina> = newId<Maquina>(),
        @Serializable(UUIDSerializer::class)
        val numSerie: UUID=UUID.randomUUID(),
        val marca: String,
        val modelo: String,
        @Serializable(LocalDateSerializer::class)
        val fechaAdquisicion: LocalDate,
        val descripcion: String,
        val tipo: TipoMaquina
    )




