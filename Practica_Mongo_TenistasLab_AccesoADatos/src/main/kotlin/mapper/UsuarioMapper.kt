package mapper

import dto.UsuarioDTO
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.litote.kmongo.id.toId
import repositories.pedidos.PedidosRepositoryImpl
import database.Usuario as UsuarioEntity
import models.Usuario as UsuarioModel

/**
 * Metodo que pasa el modelo usuario al modelo usuario utilizado en la cache(sql)
 *
 * @return
 */

suspend fun UsuarioEntity.toUserModel(): UsuarioModel {
    return UsuarioModel(
        id = ObjectId(id).toId(),
        uuidUsuario = uuidUsuario,
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password.toByteArray(),
        perfil = TipoPerfil.valueOf(perfil),
        turno = ObjectId(turno).toId(),
        pedido = PedidosRepositoryImpl().findAll().filter { it.usuario.toString() == id }.toList()
    )
}

/**
 *
 *
 * @return
 */

fun UsuarioModel.toUserEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = id.toString(),
        uuidUsuario = uuidUsuario,
        nombre = nombre,
        apellido = apellido,
        email = email,
        password = password.toString(),
        perfil = perfil.name,
        turno = turno.toString(),
        pedido = pedido.toString()
    )
}

fun UsuarioModel.toUsuarioDto(): UsuarioDTO{
    return UsuarioDTO(
        id = id.toString(),
        uuidUsuario = uuidUsuario,
        nombre = nombre,
        apellido = apellido,
        email = email,
        perfil = perfil.toString(),
        turno = turno.toString(),
        pedido = pedido.toString()
    )
}