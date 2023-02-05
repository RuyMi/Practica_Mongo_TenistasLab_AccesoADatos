package models

import kotlinx.serialization.Serializable
import models.enums.TipoPerfil
import services.password.Password

/**
 * 
 *
 * @property address
 * @property company
 * @property email
 * @property id
 * @property name
 * @property phone
 * @property username
 * @property website
 */

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
        pedido = null
    )
}

fun perfilAleatorio(): TipoPerfil {
    return mutableListOf<TipoPerfil>(TipoPerfil.USUARIO, TipoPerfil.ADMINISTRADOR, TipoPerfil.ENCORDADOR).random()
}
