package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.UUIDSerializer
import java.util.*

@Serializable
data class Producto(
    @BsonId @Contextual
    val id: Id<Producto> = newId<Producto>(), // TODO Comprobar si hace falta tener el producto en lo de newId
    @Serializable(UUIDSerializer::class)
    val uuidProducto: UUID = UUID.randomUUID(),
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stock: Int
)
