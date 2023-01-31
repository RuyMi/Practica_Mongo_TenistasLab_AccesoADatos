import kotlinx.coroutines.runBlocking
import repositories.usuario.UsuarioRepositoryKtorfit

fun main(): Unit = runBlocking{
    UsuarioRepositoryKtorfit().findAll()
}