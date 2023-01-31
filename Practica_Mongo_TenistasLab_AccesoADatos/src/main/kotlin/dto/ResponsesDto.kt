package dto

import kotlinx.serialization.Serializable
import models.Usuario
import models.UsuarioAPI

@Serializable
data class GetAllDto(
    var data: List<UsuarioAPI>? = null,

)

@Serializable
data class GetByIdDto(
    var data: Usuario? = null,
    var support: SupportDto? = null,
)

@Serializable
data class CreateDto(
    var id: Long = 0,
    var first_name: String?,
    var last_name: String?,
    var avatar: String?,
    var email: String?,
    var createdAt: String?
)

@Serializable
data class UpdateDto(
    var id: Long = 0,
    var first_name: String,
    var last_name: String,
    var avatar: String?,
    var email: String?,
    var updatedAt: String
)


@Serializable
data class SupportDto(
    var url: String? = null,
    var text: String? = null
)

@Serializable
data class TokenDto(
    var token: String? = null
)

