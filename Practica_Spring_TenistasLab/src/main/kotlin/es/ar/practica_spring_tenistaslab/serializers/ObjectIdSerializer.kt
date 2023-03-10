package es.ar.practica_spring_tenistaslab.serializers


import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.types.ObjectId
import java.time.LocalDate

object ObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    /**
     * Deserializador
     *
     * @param decoder
     * @return
     */
    override fun deserialize(decoder: Decoder): ObjectId {
        return ObjectId(decoder.decodeString())
    }

    /**
     * Serializador
     *
     * @param encoder
     * @param value
     */
    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(value.toString())
    }


}