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
    val id: Id<Usuario> = newId<Usuario>(),
    @Serializable(UUIDSerializer::class)
    val uuidUsuario: UUID = UUID.randomUUID(),
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val perfil: TipoPerfil,
    val turno: Id<Turno>?,
    val pedido: List<Pedidos>?
)