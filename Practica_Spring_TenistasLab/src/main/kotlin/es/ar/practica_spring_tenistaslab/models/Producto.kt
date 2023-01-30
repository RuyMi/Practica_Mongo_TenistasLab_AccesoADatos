package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

import org.springframework.data.mongodb.core.mapping.Document
import serializers.UUIDSerializer
import java.util.*

@Document("producto")
data class Producto(
    @Id
    val id: ObjectId= ObjectId.get(), // TODO Comprobar si hace falta tener el producto en lo de newId
    @Serializable(UUIDSerializer::class)
    val uuidProducto: UUID = UUID.randomUUID(),
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stock: Int
)