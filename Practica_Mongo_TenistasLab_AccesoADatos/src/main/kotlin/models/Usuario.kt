package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.enums.TipoPerfil
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializers.UUIDSerializer
import java.util.UUID

@Serializable
data class Usuario(
    @BsonId @Contextual
    val id: Id<Usuario> = newId(),
    val uuidUsuario: String = UUID.randomUUID().toString(),
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: ByteArray,
    val perfil: TipoPerfil,
    val turno: Id<Turno>?,
    val pedido: List<Pedidos>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Usuario

        if (id != other.id) return false
        if (uuidUsuario != other.uuidUsuario) return false
        if (nombre != other.nombre) return false
        if (apellido != other.apellido) return false
        if (email != other.email) return false
        if (!password.contentEquals(other.password)) return false
        if (perfil != other.perfil) return false
        if (turno != other.turno) return false
        if (pedido != other.pedido) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uuidUsuario.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + apellido.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + perfil.hashCode()
        result = 31 * result + (turno?.hashCode() ?: 0)
        result = 31 * result + (pedido?.hashCode() ?: 0)
        return result
    }
}