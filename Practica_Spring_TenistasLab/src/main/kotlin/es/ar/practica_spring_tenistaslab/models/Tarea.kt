package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import es.ar.practica_spring_tenistaslab.serializers.UUIDSerializer
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Document("tarea")
data class Tarea(
    @Id
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidTarea: String = UUID.randomUUID().toString(),
    @DocumentReference(lookup = "{'producto':?#{#self._id} }")
    val producto: Producto,
    val precio: Double,
    val descripcion: String,
    @DocumentReference(lookup = "{'usuarios':?#{#self._id} }")
    val empleado:Usuario,
    @DocumentReference(lookup = "{'turno':?#{#self._id} }")
    val turno:Turno?,
    val estadoCompletado:Boolean,
    @DocumentReference(lookup = "{'maquinas':?#{#self._id} }")
    val maquina: Maquina?,
    val pedido:Pedidos
) {
    override fun toString(): String {
        return "Tarea(id=$id, uuidTarea=$uuidTarea, producto=$producto, precio=$precio, descripcion='$descripcion', empleado=${empleado.nombre}, ${empleado.apellido} turno=$turno, estadoCompletado=$estadoCompletado, maquina=$maquina, pedido=$pedido)"
    }
}