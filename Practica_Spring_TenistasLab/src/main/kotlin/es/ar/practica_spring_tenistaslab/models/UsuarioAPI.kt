package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Serializable
import models.Address
import models.Company
import models.enums.TipoPerfil
import es.ar.practica_spring_tenistaslab.services.password.Password


@Serializable
data class UsuarioAPI(
    val address: Address,
    val company: Company,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)
fun UsuarioAPI.toUsuario(): Usuario{
    return Usuario(
        nombre = this.name,
        apellido = this.username,
        email = this.email,
        password = Password().encriptar(this.name),
        perfil = perfilAleatorio(),
        turno = null,
    )
}

fun perfilAleatorio(): TipoPerfil {
    return mutableListOf<TipoPerfil>(TipoPerfil.USUARIO, TipoPerfil.ADMINISTRADOR, TipoPerfil.ENCORDADOR).random()
}