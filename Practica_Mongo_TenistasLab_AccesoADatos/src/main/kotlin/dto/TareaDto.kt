package dto

import kotlinx.serialization.Serializable

/**
 * Clase TareaDTO
 *
 * @property id ID de la tarea
 * @property uuidTarea UUID de la tarea
 * @property producto Producto que tiene esa tarea
 * @property precio Precio de la tarea
 * @property descripcion Descripción de que hace la tarea
 * @property empleado Empleado que hace la tarea
 * @property turno Turno en el que se hace la tarea
 * @property estadoCompletado True si se ha completado
 * @property maquina Maquina que ha realizado la tarea
 * @property pedido Pedido al que pertenece la tarea
 */
@Serializable
data class TareaDto(
    val id:  String,
    val uuidTarea: String,
    val producto:  String,
    val precio: Double,
    val descripcion: String,
    val empleado:  String,
    val turno: String,
    val estadoCompletado:Boolean,
    val maquina:  String?,
    val pedido: String
)
