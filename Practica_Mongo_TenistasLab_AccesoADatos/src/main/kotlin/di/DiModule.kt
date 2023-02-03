package di


import controller.Controlador
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import models.Turno
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import repositories.maquina.MaquinaRepository
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepository
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepository
import repositories.producto.ProductoRepositoryImpl
import repositories.tarea.TareasRepository
import repositories.tarea.TareasRepositoryImpl
import repositories.tarea.TareasRepositoryKtorfit
import repositories.turno.TurnoRepository
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.RemoteCachedRepositoryUsuario
import repositories.usuario.UsuarioRepository
import repositories.usuario.UsuarioRepositoryImpl
import repositories.usuario.UsuarioRepositoryKtorfit
import services.usuarios.UsuariosService

//https://insert-koin.io/docs/reference/koin-annotations/modules
/**
 * Clase que se encarga de generar las dependencias con Koin
 *
 */
@Module
@ComponentScan("koin")
class DiAnnotationModule {
    val DiDslModule = module {
        // StringFormat
        single<StringFormat>(named("StringFormatJson")) { Json { prettyPrint = true } }

        // Repository
        single<MaquinaRepository> { MaquinaRepositoryImpl() }
        single<PedidosRepository> { PedidosRepositoryImpl() }
        single<ProductoRepository> { ProductoRepositoryImpl() }
        single<TareasRepository> { TareasRepositoryImpl() }
        single<TareasRepositoryKtorfit> { TareasRepositoryKtorfit() }
        single<TurnoRepository> { TurnoRepositoryImpl() }
        single<UsuarioRepositoryKtorfit> { UsuarioRepositoryKtorfit() }
        single<UsuarioRepository> { UsuarioRepositoryImpl() }
        single<RemoteCachedRepositoryUsuario> { RemoteCachedRepositoryUsuario() }
        single<UsuariosService> { UsuariosService() }


        // Controlador con Json
        single(named("ControladorTenistas")) { Controlador(get(), get(),
            get(), get(), get(), get(),
            get(), get(), get()) }

    }
}

