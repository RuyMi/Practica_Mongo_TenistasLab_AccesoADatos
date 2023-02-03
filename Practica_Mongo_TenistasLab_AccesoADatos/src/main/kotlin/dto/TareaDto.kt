package dto

import kotlinx.serialization.Serializable

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
