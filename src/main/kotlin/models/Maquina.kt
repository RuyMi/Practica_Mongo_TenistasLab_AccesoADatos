package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
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
 */
@Serializable
sealed class Maquina() {

    /**
     * Maquina personalizacion
     *
     * @property id
     * @property numSerie
     * @property marca
     * @property modelo
     * @property fechaAdquisicion
     * @property swingweight
     * @property balance
     * @property rigidez
     * @constructor Create empty Maquina personalizacion
     */
    @Serializable
    data class MaquinaPersonalizacion(
        @BsonId @Contextual
        val id: Id<MaquinaPersonalizacion> = newId<MaquinaPersonalizacion>(),
        @Serializable(UUIDSerializer::class)
        val numSerie: UUID=UUID.randomUUID(),
        val marca: String,
        val modelo: String,
        @Serializable(LocalDateSerializer::class)
        val fechaAdquisicion: LocalDate,
        val swingweight: Boolean,
        val balance: Double,
        val rigidez: Double
    ){
    }

    /**
     * Maquina encordadora
     *
     * @property id
     * @property numSerie
     * @property marca
     * @property modelo
     * @property fechaAdquisicion
     * @property automatico
     * @property tensionMaxima
     * @property tensionMinima
     * @constructor Create empty Maquina encordadora
     */
    @Serializable
    data class MaquinaEncordadora(
        @BsonId @Contextual
        val id: Id<MaquinaEncordadora> = newId<MaquinaEncordadora>(),
        @Serializable(UUIDSerializer::class)
        val numSerie: UUID=UUID.randomUUID(),
        val marca: String,
        val modelo: String,
        @Serializable(LocalDateSerializer::class)
        val fechaAdquisicion: LocalDate,
        val automatico: Boolean,
        val tensionMaxima: Double,
        val tensionMinima: Double
    )




}
