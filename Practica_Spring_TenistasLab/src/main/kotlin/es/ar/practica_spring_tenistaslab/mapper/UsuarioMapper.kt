package es.ar.practica_spring_tenistaslab.mapper


import es.ar.practica_spring_tenistaslab.dto.UsuarioDTO
import es.ar.practica_spring_tenistaslab.models.Usuario as UsuarioModel

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