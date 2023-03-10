package models

import dto.UsuarioDTO
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.UUIDSerializer
import java.util.*


/**
 * Tarea
 *
 * @property id
 * @property uuidTarea
 * @property producto
 * @property precio
 * @property descripcion
 * @property empleado
 * @property turno
 * @property estadoCompletado
 * @property maquina
 * @property pedido
 */
@Serializable
data class Tarea(
    @BsonId @Contextual
    val id: Id<Tarea> = newId<Tarea>(),
    val uuidTarea: String,
    val producto: Producto,
    val precio: Double,
    val descripcion: String,
    val empleado: UsuarioDTO,
    val turno:Turno,
    val estadoCompletado:Boolean,
    val maquina: Maquina?,
    val pedido:Pedidos
)


