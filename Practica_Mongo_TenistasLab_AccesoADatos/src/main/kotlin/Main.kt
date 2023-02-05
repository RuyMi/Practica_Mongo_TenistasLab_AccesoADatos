import controller.Controlador
import db.*
import di.DiAnnotationModule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.collect
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Pedidos
import models.Producto
import models.Tarea
import models.Usuario
import models.enums.TipoEstado
import models.enums.TipoPerfil
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule
import org.litote.kmongo.newId
import repositories.usuario.UsuarioRepositoryImpl
import services.password.Password
import services.sqldelight.SqlDeLightClient
import java.util.*
import kotlin.system.exitProcess


private val json = Json {
    prettyPrint = true
    allowStructuredMapKeys = true
    serializersModule = IdKotlinXSerializationModule
}
private val logger = KotlinLogging.logger{}
// ¡ATENCIÓN! Esto borrará la base de datos y la volverá a inicializar con datos por defecto
private const val INICIALIZAR_DATOS = true
var usuarioActual: Usuario? = null


/**
 * Funcion principal Main
 *
 */
fun main(){
    startKoin {
        printLogger()
        modules(DiAnnotationModule().DiDslModule)
    }
    KoinApp().run()
}

/**
 * Init data base
 *
 */
suspend fun initDataBase() {
    borrarDataBase()
    val meterAdmin = Usuario(
        newId(),
        UUID.randomUUID().toString(),
        "Administrador",
        "Prueba",
        "admin@admin.com",
        Password().encriptar("1"),
        TipoPerfil.ADMINISTRADOR,
        null,
        null
    )
    println("La contraseña cifrada es = "+ meterAdmin.password)
    UsuarioRepositoryImpl().save(meterAdmin)
}

suspend fun borrarDataBase() {
    MongoDbManager.database.drop()
}

class KoinApp : KoinComponent {

    private val controlador: Controlador by inject(named("ControladorTenistas"))
    fun run() {
        controller()
    }

    private fun controller() = runBlocking {
        val init = launch {
            if(INICIALIZAR_DATOS) {
                initDataBase()
            }
            usuarioActual = iniciarSesion()
            controlador.usuarioActual = usuarioActual
        }
        init.join()
        launch {
            println("✔ Escuchando cambios en Usuarios...")
            controlador.watchUsuarios()
                .collect {
                    println("Evento Usuario: ${it.operationType.value}")
                }
        }

        launch {meterDatos(controlador)}.join()
        //Lanzamos una corutina para refrescar la cache de usuarios con la api cada 60 segundos
        launch {
            controlador.refreshUsuarios()
        }

        launch {
            // Lista de un pedido completo en json
            val pedido = controlador.encontrarPedidoUUID("45c3ca42-dc8f-46c7-9dfe-ff8fd786a77f")
            val tareas = mutableListOf<Tarea>()
            controlador.listarTareas().collect {
                tareas.add(it)
            }
            val tareasJson = tareas.filter { it.pedido.uuidPedidos == pedido!!.uuidPedidos }
            val tarea1 = json.encodeToString(pedido)
            val tarea2 = json.encodeToString(tareasJson)
            println(
                """Pedido: $tarea1
            Las tareas de este producto eran: $tarea2
        """.trimMargin()
            )
        }

        launch {
            //Listado de pedidos pendientes en JSON
            val pedidosList = mutableListOf<Pedidos>()
            controlador.listarPedidos().collect {
                pedidosList.add(it)
            }
            val pedidosPen = pedidosList.filter { it.estado == TipoEstado.EN_PROCESO }
            val pedidosPenjson = json.encodeToString(pedidosPen)
            println("Listado de pedidos pendientes: $pedidosPenjson")
        }

        launch {
            //Listado de pedidos completados en JSON
            val pedidosList = mutableListOf<Pedidos>()
            controlador.listarPedidos().collect {
                pedidosList.add(it)
            }
            val pedidosCom = pedidosList.filter { it.estado != TipoEstado.EN_PROCESO }
            val pedidosComjson = json.encodeToString(pedidosCom)
            println("Listado de pedidos completados: $pedidosComjson")
        }

        launch {
            //Listado de productos y servicios en JSON
            val productosList = mutableListOf<Producto>()
            controlador.listarProductos().collect {
                productosList.add(it)
            }
            val productosjson = json.encodeToString(productosList)
            println(
                """Productos disponibles: 
        |$productosjson
        |
        |Servicios que ofrecemos:
        | -> Adquisición
        | -> Personalizacion
        | -> Encordar
    """.trimMargin()
            )
        }

        launch {
            //Listado de asignaciones para los encordadores por fecha en JSON
            // Hemos entendido que debemos sacar por cada empleado, sus tareas realizadas ordenadas por hora
            val tareasByEmpleadoSortFecha = mutableListOf<Tarea>()
            controlador.listarTareas()
                .onCompletion { logger.debug { "Tareas recolectadas correctamente" } }
                .collect { tareasByEmpleadoSortFecha.add(it) }

            val ordenadoTareas = tareasByEmpleadoSortFecha.sortedBy { it.turno.fechaFin }.groupBy { it.empleado }
            val jsonTareas = json.encodeToString(ordenadoTareas)
            println("""Listado de las tareas agrupadas por empleado y ordenadas por fecha: $jsonTareas""")
        }
        //mostrarMenuPrincipal(usuarioActual)


    }

    fun mostrarMenuPrincipal(usuario: Usuario) {
        when (usuario.perfil) {
            TipoPerfil.ENCORDADOR -> {
                println(
                    """
                Seleccione una de las siguientes opciones:
                1. Crear Pedido
                2. Modificar Pedido
                3. Crear Tarea
                4. Modificar Tarea
                5. Crear Turno (Bloqueado)
                6. Modificar Turno (Bloqueado)
                7. Crear Usuarios (Bloqueado)
                8. Modificar Usuarios (Bloqueado)
                9. Listar datos (Bloqueado)
                Cambiar. Cambiar usuario
                Exit. Salir del programa
            """.trimIndent()
                )
            }

            TipoPerfil.USUARIO -> {
                println(
                    """
                Seleccione una de las siguientes opciones:
                1. Crear Pedido
                2. Modificar Pedido 
                3. Crear Tarea (Bloqueado)
                4. Modificar Tarea (Bloqueado)
                5. Crear Turno (Bloqueado)
                6. Modificar Turno (Bloqueado)
                7. Crear Usuarios (Bloqueado)
                8. Modificar Usuarios (Bloqueado)
                9. Listar datos (Bloqueado)
                Cambiar. Cambiar usuario
                Exit. Salir del programa
            """.trimIndent()
                )
            }

            TipoPerfil.ADMINISTRADOR -> {
                println(
                    """
                Seleccione una de las siguientes opciones:
                1. Crear Pedido
                2. Modificar Pedido 
                3. Crear Tarea 
                4. Modificar Tarea 
                5. Crear Turno 
                6. Modificar Turno 
                7. Crear Usuarios 
                8. Modificar Usuarios 
                9. Listar datos 
                Cambiar. Cambiar usuario
                Exit. Salir del programa
            """.trimIndent()
                )
            }
        }
    }

}
suspend fun iniciarSesion(): Usuario {
    println("Bienvenido al sistema, por favor introduzca su correo electronico y su contraseña para acceder")
    val usuario = "admin@admin.com"
    val password = "1"
    try {
        val coincidente = UsuarioRepositoryImpl().findAll().first { it.email == usuario && Password().verificar(password, it.password) }
        println("Bienvenido: ${coincidente.nombre} ${coincidente.apellido}, eres un ${coincidente.perfil}")
        return coincidente
    } catch (e: Exception) {
        println("Usuario no válido... Saliendo del sistema")
        exitProcess(0)
    }


}

private suspend fun meterDatos(controlador: Controlador) = withContext(Dispatchers.IO){
    val tarea1= launch{
        getTurnos().forEach { controlador.guardarTurno(it) }
        val listaTurnos = controlador.listarTurnos()
        listaTurnos.collect { println(it) }
    }
    tarea1.join()

    val tarea2= launch{
        getUsuarios().forEach { it?.let { it1 -> controlador.guardarUsuario(it1) } }
        val listaUsuarios = controlador.listarUsuarios()
        listaUsuarios.collect { println(it) }
    }
    tarea2.join()

    val tarea3= launch{
        getMaquinas().forEach { controlador.guardarMaquina(it) }
        val listaMaquinas = controlador.listarMaquinas()
        listaMaquinas.collect { println(it) }
    }
    tarea3.join()


    val tarea4= launch{
        getProductos().forEach { controlador.guardarProducto(it) }
        val listaProductos = controlador.listarProductos()
        listaProductos.collect { println(it) }
    }
    tarea4.join()




    val tarea5= launch{
        getPedidos().forEach { it?.let { it1 -> controlador.guardarPedido(it1) } }
        val listaPedidos = controlador.listarPedidos()
        listaPedidos.collect { println(it) }
    }
    tarea5.join()


    val tarea6= launch{
        getTareas().forEach { controlador.guardarTarea(it) }
        val listaTareas = controlador.listarTareas()
        listaTareas.collect { println(it) }
    }
    tarea6.join()

}
