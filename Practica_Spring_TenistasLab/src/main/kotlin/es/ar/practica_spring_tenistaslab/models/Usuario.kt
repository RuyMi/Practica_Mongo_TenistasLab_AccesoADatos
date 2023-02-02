package es.ar.practica_spring_tenistaslab.models

import kotlinx.serialization.Serializable
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import serializers.UUIDSerializer
import java.util.*

@Document("usuario")
data class Usuario(
    @Id
    val id: ObjectId = ObjectId.get(),
    @Serializable(UUIDSerializer::class)
    val uuidUsuario:String = UUID.randomUUID().toString(),
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: ByteArray,
    val perfil: TipoPerfil,
    @DocumentReference()
    val turno: String?,//es el id
    @DocumentReference()
    val pedido: List<String>?
)