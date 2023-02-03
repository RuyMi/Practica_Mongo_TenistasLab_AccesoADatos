package es.ar.practica_spring_tenistaslab.models

import es.ar.practica_spring_tenistaslab.serializers.ObjectIdSerializer
import kotlinx.serialization.Serializable
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import es.ar.practica_spring_tenistaslab.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import java.util.*

@Serializable
@Document("usuario")
data class Usuario(
    @Id @Serializable(ObjectIdSerializer::class)
    val id: ObjectId = ObjectId.get(),
    val uuidUsuario:String = UUID.randomUUID().toString(),
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: ByteArray,
    val perfil: TipoPerfil,
    //@DocumentReference()
    val turno: Turno?,//es el id

){

}