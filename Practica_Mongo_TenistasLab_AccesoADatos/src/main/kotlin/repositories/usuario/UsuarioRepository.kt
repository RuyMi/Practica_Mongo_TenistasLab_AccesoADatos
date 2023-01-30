package repositories.usuario

import models.Usuario
import org.litote.kmongo.Id
import repositories.CrudRepository

interface UsuarioRepository:CrudRepository<Usuario, Id<Usuario>> {
}