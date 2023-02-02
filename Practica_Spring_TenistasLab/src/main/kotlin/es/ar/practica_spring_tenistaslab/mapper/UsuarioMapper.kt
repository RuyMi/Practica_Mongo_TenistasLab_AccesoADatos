package es.ar.practica_spring_tenistaslab.mapper


import es.ar.practica_spring_tenistaslab.dto.UsuarioDTO
import es.ar.practica_spring_tenistaslab.repositories.PedidosRepository
import es.ar.practica_spring_tenistaslab.repositories.TurnoRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import models.enums.TipoPerfil
import org.bson.types.ObjectId


import database.Usuario as UsuarioEntity
import es.ar.practica_spring_tenistaslab.models.Usuario as UsuarioModel





suspend fun UsuarioEntity.toUserModel(): UsuarioModel {
    return UsuarioModel(
        id = ObjectId(id),
        uuidUsuario = uuidUsuario,
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password.toByteArray(),
        perfil = TipoPerfil.valueOf(perfil),
        turno = turno,
        pedido = listOf(pedido)//TODO MIRAR LOS MODELOS Y REFERENCIAS CON LA CACHE
    )
}

fun UsuarioModel.toUserEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = id.toString(),
        uuidUsuario = uuidUsuario,
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password.toString(),
        perfil = perfil.name,
        turno = turno.toString(),
        pedido = pedido.toString()
    )
}

fun UsuarioModel.toUsuarioDto(): UsuarioDTO {
    return UsuarioDTO(
        id = id,
        uuidUsuario = uuidUsuario,
        nombre = nombre,
        apellido = apellido,
        email = email,
        perfil = perfil,
        turno = turno,
        pedido = pedido
    )
}