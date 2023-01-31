

import controller.Controlador
import db.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Tarea
import models.Usuario
import models.enums.TipoEstado
import models.enums.TipoPerfil
import org.litote.kmongo.newId
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
import repositories.tarea.TareasRepositoryImpl
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.UsuarioRepositoryImpl
import services.password.Password

import java.util.*
import kotlin.system.exitProcess

private val json = Json {
    prettyPrint = true
    allowStructuredMapKeys = true
}
// ¡ATENCION! Esto borrará la base de datos y la volverá a inicializar con datos por defecto
private val inicializarDatos = true

/**
 * Main
 *
 * @param args
 */
fun main(args: Array<String>) = runBlocking {
    var usuarioActual: Usuario? = null
    if(inicializarDatos) {
        val init = launch {
            initDataBase()
            usuarioActual = iniciarSesion()
        }
        init.join()
    }


    val controlador = Controlador(
        MaquinaRepositoryImpl(),
        PedidosRepositoryImpl(),
        ProductoRepositoryImpl(),
        TareasRepositoryImpl(),
        UsuarioRepositoryImpl(),
        TurnoRepositoryImpl(),
        usuarioActual!!
    )
    meterDatos(controlador)
    // Lista de un pedido completo en json
    val pedido = controlador.encontrarPedidoUUID(UUID.fromString("45c3ca42-dc8f-46c7-9dfe-ff8fd786a77f"))
    val tareas = controlador.listarTareas().filter { it.pedido.uuidPedidos == pedido!!.uuidPedidos }
    val tarea1 = json.encodeToString(pedido)
    val tarea2 = json.encodeToString(tareas)
    println(
        """Pedido: $tarea1
        Las tareas de este producto eran: $tarea2
    """.trimMargin()
    )

    //Listado de pedidos pendientes en JSON
    val pedidosPen = controlador.listarPedidos().filter { it.estado == TipoEstado.EN_PROCESO }
    val pedidosPenjson = json.encodeToString(pedidosPen)
    println("Listado de pedidos pendientes: $pedidosPenjson")

    //Listado de pedidos completados en JSON
    val pedidosCom = controlador.listarPedidos().filter { it.estado != TipoEstado.EN_PROCESO }
    val pedidosComjson = json.encodeToString(pedidosCom)
    println("Listado de pedidos completados: $pedidosComjson")

    //Listado de productos y servicios en JSON
    val productos = controlador.listarProductos()
    val productosjson = json.encodeToString(productos)
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

    //Listado de asignaciones para los encordadores por fecha en JSON
    //* Hemos entendido que debemos sacar por cada empleado, sus tareas realizadas ordenadas por hora
    val tareasByEmpleadoSortFecha = mutableListOf<Tarea>()
    controlador.listarTareas().onEach { tareasByEmpleadoSortFecha.add(it) }
        .onCompletion { "Tareas recolectadas correctamente" }
        .collect()
    val ordenadoTareas= tareasByEmpleadoSortFecha.sortedBy { it.turno.fechaFin }.groupBy { it.empleado }
    val tareasByEmpleadoSortFechajson = json.encodeToString(ordenadoTareas)
    println("""Listado de las tareas agrupadas por empleado y ordenadas por fecha: $tareasByEmpleadoSortFechajson""")
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

suspend fun iniciarSesion(): Usuario {
    println("Bienvenido al sistema, por favor introduzca su correo electronico y su contraseña para acceder")
    val usuario = readln()
    val password = readln()
    try {
        val coincidente = UsuarioRepositoryImpl().findAll().first { it.email == usuario && Password().verificar(password, it.password.toByteArray()) }
        println("Bienvenido: ${coincidente.nombre} ${coincidente.apellido}, eres un ${coincidente.perfil}")
        return coincidente
    } catch (e: Exception) {
        println("Usuario no válido... Saliendo del sistema")
        exitProcess(0)
    }


}

private suspend fun meterDatos(controlador: Controlador) {
    getTurnos().forEach { controlador.guardarTurno(it) }
    val listaTurnos = controlador.listarTurnos()
    listaTurnos.collect { println(it) }

    getUsuarios().forEach { it?.let { it1 -> controlador.guardarUsuario(it1) } }
    val listaUsuarios = controlador.listarUsuarios()
    listaUsuarios.collect { println(it) }

    getMaquinas().forEach { controlador.guardarMaquina(it) }
    val listaMaquinas = controlador.listarMaquinas()
    listaMaquinas.collect { println(it) }



    getProductos().forEach { controlador.guardarProducto(it) }
    val listaProductos = controlador.listarProductos()
    listaProductos.collect { println(it) }

    getPedidos().forEach { it?.let { it1 -> controlador.guardarPedido(it1) } }
    val listaPedidos = controlador.listarPedidos()
    listaPedidos.collect { println(it) }

    getTareas().forEach { controlador.guardarTarea(it) }
    val listaTareas = controlador.listarTareas()
    listaTareas.collect { println(it) }
}

/**
 * Init data base
 *
 */
suspend fun initDataBase() {
    borrarDataBase()
    val meterAdmin = Usuario(
        newId(),
        UUID.randomUUID(),
        "Administrador",
        "Prueba",
        "admin@admin.com",
        Password().encriptar(readln()),
        TipoPerfil.ADMINISTRADOR,
        null,
        null
    )
    println(meterAdmin.password)
    UsuarioRepositoryImpl().save(meterAdmin)

}

fun borrarDataBase() {


}
