package mapper

import dto.TareaDto
import dto.UsuarioDTO
import models.*
import org.litote.kmongo.newId

fun Tarea.toTareaDto(): TareaDto {
    return TareaDto(
          id = id.toString(),
          uuidTarea = uuidTarea,
          producto = producto.toString(),
          precio = precio,
          descripcion = descripcion,
          empleado = empleado.toString(),
          turno = turno.toString(),
          estadoCompletado =estadoCompletado,
          maquina = maquina.toString(),
          pedido = pedido.toString()
    )
}