package es.ar.practica_spring_tenistaslab.dto

import es.ar.practica_spring_tenistaslab.models.Pedidos
import es.ar.practica_spring_tenistaslab.models.Turno
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


import models.enums.TipoPerfil

import org.bson.types.ObjectId

import org.springframework.data.annotation.Id


@Serializable
data class UsuarioDTO(
    @Id
    val id: ObjectId = ObjectId.get(),
    val uuidUsuario: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val perfil: TipoPerfil,
    val turno: String?,
    val pedido: MutableSet<String>?
) {
}