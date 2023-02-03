package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.dto.UsuarioDTO
import es.ar.practica_spring_tenistaslab.serializers.ObjectIdSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import es.ar.practica_spring_tenistaslab.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import java.util.*

@Serializable
@Document("tarea")
data class Tarea(
    @Id @Serializable(ObjectIdSerializer::class)
    val id: ObjectId = ObjectId.get(),
    val uuidTarea: String = UUID.randomUUID().toString(),
    @DocumentReference(lookup = "{'producto':?#{#self._id} }")
    val producto: Producto,
    val precio: Double,
    val descripcion: String,
    val empleado:UsuarioDTO,
    val turno:Turno,
    val estadoCompletado:Boolean,
    @DocumentReference(lookup = "{'maquinas':?#{#self._id} }")
    val maquina: Maquina?,
    val pedido:Pedidos
) {
    override fun toString(): String {
        return "Tarea(id=$id, uuidTarea=$uuidTarea, producto=$producto, precio=$precio, descripcion='$descripcion', empleado=${empleado.nombre}, ${empleado.apellido} turno=$turno, estadoCompletado=$estadoCompletado, maquina=$maquina, pedido=$pedido)"
    }
}