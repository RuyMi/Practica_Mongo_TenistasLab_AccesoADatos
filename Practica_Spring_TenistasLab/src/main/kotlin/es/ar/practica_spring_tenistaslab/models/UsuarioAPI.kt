package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Serializable
import models.Address
import models.Company


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