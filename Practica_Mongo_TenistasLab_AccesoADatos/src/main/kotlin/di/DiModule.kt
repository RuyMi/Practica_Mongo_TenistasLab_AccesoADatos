package di


import controller.Controlador
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import models.Turno
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
import repositories.tarea.TareasRepositoryImpl
import repositories.tarea.TareasRepositoryKtorfit
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.RemoteCachedRepositoryUsuario
import repositories.usuario.UsuarioRepositoryImpl
import repositories.usuario.UsuarioRepositoryKtorfit

//https://insert-koin.io/docs/reference/koin-annotations/modules
@Module
@ComponentScan("koin")
class DiAnnotationModule

// si quiero hacerlo manuales
// https://insert-koin.io/docs/reference/koin-core/dsl
val DiDslModule = module {
    // StringFormat
    single<StringFormat>(named("StringFormatJson")) { Json { prettyPrint = true } }

    // Repository
    single<MaquinaRepositoryImpl> { MaquinaRepositoryImpl() }
    single<PedidosRepositoryImpl> { PedidosRepositoryImpl() }
    single<ProductoRepositoryImpl> { ProductoRepositoryImpl() }
    single<TareasRepositoryImpl> { TareasRepositoryImpl() }
    single<TareasRepositoryKtorfit> { TareasRepositoryKtorfit() }
    single<TurnoRepositoryImpl> { TurnoRepositoryImpl() }
    single<UsuarioRepositoryKtorfit> { UsuarioRepositoryKtorfit() }
    single<UsuarioRepositoryImpl> { UsuarioRepositoryImpl() }
    single<RemoteCachedRepositoryUsuario> { RemoteCachedRepositoryUsuario(get()) }


    // Controlador con Json
    single(named("ControladorTenistas")) { Controlador(get(), get(), get(), get(), get(), get(), get(), get()) }

}

