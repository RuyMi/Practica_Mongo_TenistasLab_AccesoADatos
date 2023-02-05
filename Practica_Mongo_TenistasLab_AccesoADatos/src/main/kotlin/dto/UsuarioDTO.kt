package dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.Pedidos
import models.Turno
import models.Usuario
import models.enums.TipoPerfil
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

/**
 * Clase UsuarioDTO
 *
 * @property id
 * @property uuidUsuario
 * @property nombre
 * @property apellido
 * @property email
 * @property perfil
 * @property turno
 * @property pedido
 */
@Serializable
data class UsuarioDTO(
    val id: String,
    val uuidUsuario: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val perfil: String,
    val turno: String,
    val pedido: String
) {
}