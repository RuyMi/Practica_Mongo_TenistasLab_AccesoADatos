package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.enums.TipoPerfil
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import serializers.UUIDSerializer
import java.util.*

@Document("usuario")
data class Usuario(
    @BsonId @Contextual
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidUsuario: UUID = UUID.randomUUID(),
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val perfil: TipoPerfil,
    val turno: Turno,
    @DocumentReference()//lookup = "{'tenistas':?#{#self._id} }"
    val pedido: List<Pedidos>
)