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

@Serializable
data class UsuarioDTO(
    @BsonId @Contextual
    val id: Id<Usuario>,
    val uuidUsuario: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val perfil: TipoPerfil,
    @Contextual
    val turno: Id<Turno>?,
    @Contextual
    val pedido: List<Pedidos>?
) {
}