package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.UUIDSerializer
import java.util.*

/**
 * Producto
 *
 * @property id
 * @property uuidProducto
 * @property marca
 * @property modelo
 * @property precio
 * @property stock
 */
@Serializable
data class Producto(
    @BsonId @Contextual
    val id: Id<Producto> = newId<Producto>(),
    val uuidProducto: String = UUID.randomUUID().toString(),
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stock: Int
)
