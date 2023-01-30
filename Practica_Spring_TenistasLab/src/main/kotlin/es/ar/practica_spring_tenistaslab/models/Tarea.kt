package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import serializers.UUIDSerializer
import java.util.*

@Document("tarea")
data class Tarea(
    @BsonId @Contextual
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidTarea: UUID,
    val producto: Producto,
    val precio: Double,
    val descripcion: String,
    val empleado:Usuario,
    val turno:Turno,
    val estadoCompletado:Boolean,
    val maquina: Maquina?,
    val pedido:Pedidos
) {
    override fun toString(): String {
        return "Tarea(id=$id, uuidTarea=$uuidTarea, producto=$producto, precio=$precio, descripcion='$descripcion', empleado=${empleado.nombre}, ${empleado.apellido} turno=$turno, estadoCompletado=$estadoCompletado, maquina=$maquina, pedido=$pedido)"
    }
}