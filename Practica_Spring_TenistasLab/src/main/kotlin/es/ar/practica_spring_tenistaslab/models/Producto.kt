package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.serializers.ObjectIdSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

import org.springframework.data.mongodb.core.mapping.Document
import es.ar.practica_spring_tenistaslab.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import java.util.*

@Serializable
@Document("producto")
data class Producto(
    @Id @Serializable(ObjectIdSerializer::class)
    val id: ObjectId= ObjectId.get(), // TODO Comprobar si hace falta tener el producto en lo de newId
    val uuidProducto:  String = UUID.randomUUID().toString(),
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stock: Int
)