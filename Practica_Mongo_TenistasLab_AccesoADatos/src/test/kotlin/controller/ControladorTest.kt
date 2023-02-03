package controller

import db.MongoDbManager
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mapper.toUsuarioDto
import models.*
import models.enums.TipoEstado
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.litote.kmongo.id.toId
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
import repositories.tarea.TareasRepositoryImpl
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.RemoteCachedRepositoryUsuario
import repositories.usuario.UsuarioRepositoryImpl
import repositories.usuario.UsuarioRepositoryKtorfit
import services.password.Password
import services.sqldelight.SqlDeLightClient
import services.usuarios.UsuariosService
import java.time.LocalDate
import java.time.LocalDateTime

class ControladorTest {
    val dataBaseService = MongoDbManager.database
    val controlador = Controlador(
        MaquinaRepositoryImpl(),
        PedidosRepositoryImpl(),
        ProductoRepositoryImpl(),
        TareasRepositoryImpl(),
        UsuarioRepositoryImpl(),
        TurnoRepositoryImpl(),
        UsuarioRepositoryKtorfit(),
        RemoteCachedRepositoryUsuario(),
        UsuariosService(),
        Usuario(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c32d-43e3-ba77-8083a542f425",
            "Admin",
            "Admin",
            "admin@admin.com",
            Password().encriptar("1234"),
            TipoPerfil.ADMINISTRADOR,
            null,
            null
        )
    )

    val setup = runBlocking {
        dataBaseService.drop()
        SqlDeLightClient.queries.deleteAll()
        val turnoTest =  Turno(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-4313-ba77-8083a542f425",
            LocalDateTime.of(2022, 12, 5, 8, 0),
            LocalDateTime.of(2022, 12, 5, 10, 0)
        )
        TurnoRepositoryImpl().save(turnoTest)

        val usuarioTest = Usuario(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c32d-43e3-ba77-8083a542f425",
            "Mario",
            "Sánchez",
            "mario.sanchez@gmail.com",
            Password().encriptar("marioSanchez"),
            TipoPerfil.ADMINISTRADOR,
            TurnoRepositoryImpl().findByUUID("492a7f86-c34d-4313-ba77-8083a542f425")!!.id,
            null
        )
        UsuarioRepositoryImpl().save(usuarioTest)

        val maquina = Maquina(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "a016f77a-4698-4bd3-8294-1edb74311d27",
            "nadal",
            "rojo",
            LocalDate.now(),
            "true, 20.0, 12.4",
            TipoMaquina.ENCORDAR
        )
        MaquinaRepositoryImpl().save(maquina)
        val producto = Producto(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-43e3-ba77-8183a542f425",
            "Wilson",
            "raqueta",
            20.2,
            12
        )
        ProductoRepositoryImpl().save(producto)

        val pedidoTest = Pedidos(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-43e3-ba77-8083a542f425",
            TipoEstado.RECIBIDO,
            LocalDate.now(),
            LocalDate.of(2022, 12, 12),
            LocalDate.of(2022, 12, 13),
            120.5,
            UsuarioRepositoryImpl().findByUUID("492a7f86-c32d-43e3-ba77-8083a542f425")!!.id
        )
        val pedido = PedidosRepositoryImpl().save(pedidoTest)

        val tareaTest = runBlocking {
            Tarea(
                ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
                "192a7f86-c34d-43e3-ba77-8083a542f425",
                ProductoRepositoryImpl().findByUUID("492a7f86-c34d-43e3-ba77-8183a542f425")!!,
                20.0,
                "Personalizacion",
                UsuarioRepositoryImpl().findByUUID("492a7f86-c32d-43e3-ba77-8083a542f425")!!.toUsuarioDto(),
                TurnoRepositoryImpl().findByUUID("492a7f86-c34d-4313-ba77-8083a542f425")!!,
                true,
                MaquinaRepositoryImpl().findByUUID("a016f77a-4698-4bd3-8294-1edb74311d27")!!,
                PedidosRepositoryImpl().findByUUID("492a7f86-c34d-43e3-ba77-8083a542f425")!!
            )
        }
        controlador.guardarTarea(tareaTest)
    }



    val usuarioTest = runBlocking {
        controlador.encontrarUsuarioUUID("492a7f86-c32d-43e3-ba77-8083a542f425")!!
    }
    val maquinaTest = runBlocking {
        controlador.encontrarMaquinaUUID("a016f77a-4698-4bd3-8294-1edb74311d27")!!
    }
    val productoTest = runBlocking {
        controlador.encontrarProductoUUID("492a7f86-c34d-43e3-ba77-8183a542f425")!!
    }
    val pedidoTest = runBlocking {
        controlador.encontrarPedidoUUID("492a7f86-c34d-43e3-ba77-8083a542f425")!!
    }
    val tareaTest = runBlocking {
        controlador.encontrarTareaUUID("192a7f86-c34d-43e3-ba77-8083a542f425")!!
    }
    val turnoTest = runBlocking {
        controlador.encontrarTurnoUUID("492a7f86-c34d-4313-ba77-8083a542f425")!!
    }



    @BeforeEach
    fun setUp(): Unit = runBlocking {
        dataBaseService.drop()
        SqlDeLightClient.queries.deleteAll()
        val turno =  Turno(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-4313-ba77-8083a542f425",
            LocalDateTime.of(2022, 12, 5, 8, 0),
            LocalDateTime.of(2022, 12, 5, 10, 0)
        )
        TurnoRepositoryImpl().save(turno)

        val usuarioTest = Usuario(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c32d-43e3-ba77-8083a542f425",
            "Mario",
            "Sánchez",
            "mario.sanchez@gmail.com",
            Password().encriptar("marioSanchez"),
            TipoPerfil.ADMINISTRADOR,
            TurnoRepositoryImpl().findByUUID("492a7f86-c34d-4313-ba77-8083a542f425")!!.id,
            null
        )
        UsuarioRepositoryImpl().save(usuarioTest)

        val maquina = Maquina(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "a016f77a-4698-4bd3-8294-1edb74311d27",
            "nadal",
            "rojo",
            LocalDate.now(),
            "true, 20.0, 12.4",
            TipoMaquina.ENCORDAR
        )
        MaquinaRepositoryImpl().save(maquina)
        val producto = Producto(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-43e3-ba77-8183a542f425",
            "Wilson",
            "raqueta",
            20.2,
            12
        )
        ProductoRepositoryImpl().save(producto)

        val pedidoTest = Pedidos(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-43e3-ba77-8083a542f425",
            TipoEstado.RECIBIDO,
            LocalDate.now(),
            LocalDate.of(2022, 12, 12),
            LocalDate.of(2022, 12, 13),
            120.5,
            UsuarioRepositoryImpl().findByUUID("492a7f86-c32d-43e3-ba77-8083a542f425")!!.id
        )
        val pedido = PedidosRepositoryImpl().save(pedidoTest)

        val tareaTest = runBlocking {
            Tarea(
                ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
                "192a7f86-c34d-43e3-ba77-8083a542f425",
                ProductoRepositoryImpl().findByUUID("492a7f86-c34d-43e3-ba77-8183a542f425")!!,
                20.0,
                "Personalizacion",
                UsuarioRepositoryImpl().findByUUID("492a7f86-c32d-43e3-ba77-8083a542f425")!!.toUsuarioDto(),
                TurnoRepositoryImpl().findByUUID("492a7f86-c34d-4313-ba77-8083a542f425")!!,
                true,
                MaquinaRepositoryImpl().findByUUID("a016f77a-4698-4bd3-8294-1edb74311d27")!!,
                PedidosRepositoryImpl().findByUUID("492a7f86-c34d-43e3-ba77-8083a542f425")!!
            )
        }
        controlador.guardarTarea(tareaTest)

    }


    @Test
    fun listarMaquinas(): Unit = runBlocking  {
        val maquinas = controlador.listarMaquinas().toList()
        assertAll(
            {assertEquals(1, maquinas.size)},
            { assertFalse(maquinas.isEmpty()) },
            {assertEquals(maquinaTest.id, maquinas[0].id)},
            {assertEquals(maquinaTest.numSerie, maquinas[0].numSerie)},
            {assertEquals(maquinaTest.descripcion, maquinas[0].descripcion)},
            {assertEquals(maquinaTest.marca, maquinas[0].marca)},
            {assertEquals(maquinaTest.fechaAdquisicion, maquinas[0].fechaAdquisicion)},
            {assertEquals(maquinaTest.modelo, maquinas[0].modelo)},
            {assertEquals(maquinaTest.tipo, maquinas[0].tipo)},
        )
    }

    @Test
    fun encontrarMaquinaID(): Unit = runBlocking  {
        val testID = controlador.encontrarMaquinaID(maquinaTest.id)
        Assertions.assertAll(
            { assertEquals(testID!!.id, maquinaTest.id) },
            { assertEquals(testID!!.numSerie, maquinaTest.numSerie) },
            { assertEquals(testID!!.marca, maquinaTest.marca) },
            { assertEquals(testID!!.modelo, maquinaTest.modelo) },
            { assertEquals(testID!!.descripcion, maquinaTest.descripcion) },
            { assertEquals(testID!!.fechaAdquisicion, maquinaTest.fechaAdquisicion) },
        )
    }

    @Test
    fun encontrarMaquinaUUID(): Unit = runBlocking  {
        val testUUID = controlador.encontrarMaquinaUUID(maquinaTest.numSerie)
        assertAll(
            { assertEquals(testUUID!!.id, maquinaTest.id) },
            { assertEquals(testUUID!!.numSerie, maquinaTest.numSerie) },
            { assertEquals(testUUID!!.marca, maquinaTest.marca) },
            { assertEquals(testUUID!!.modelo, maquinaTest.modelo) },
            { assertEquals(testUUID!!.descripcion, maquinaTest.descripcion) },
            { assertEquals(testUUID!!.fechaAdquisicion, maquinaTest.fechaAdquisicion) },
        )
    }

    @Test
    fun guardarMaquina(): Unit = runBlocking  {
        val testSave = controlador.guardarMaquina(maquinaTest)
        assertAll(
            { assertEquals(testSave!!.id, maquinaTest.id) },
            { assertEquals(testSave!!.numSerie, maquinaTest.numSerie) },
            { assertEquals(testSave!!.marca, maquinaTest.marca) },
            { assertEquals(testSave!!.modelo, maquinaTest.modelo) },
            { assertEquals(testSave!!.descripcion, maquinaTest.descripcion) },
            { assertEquals(testSave!!.fechaAdquisicion, maquinaTest.fechaAdquisicion) },
            )
    }

    @Test
    fun borrarMaquina(): Unit = runBlocking  {
        val testDelete = controlador.borrarMaquina(maquinaTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }

    @Test
    fun listarPedidos(): Unit = runBlocking  {
        val pedidos = controlador.listarPedidos().toList()
        assertAll(
            { assertFalse(pedidos.isEmpty()) },
            { assertEquals(1, pedidos.size) },
            { assertEquals(pedidoTest.id, pedidos[0].id) },
            { assertEquals(pedidoTest.uuidPedidos, pedidos[0].uuidPedidos) },
            { assertEquals(pedidoTest.estado, pedidos[0].estado) },
            { assertEquals(pedidoTest.fechaEntrada, pedidos[0].fechaEntrada) },
            { assertEquals(pedidoTest.fechaSalidaProgramada, pedidos[0].fechaSalidaProgramada) },
            { assertEquals(pedidoTest.fechaEntrega, pedidos[0].fechaEntrega) },
            { assertEquals(pedidoTest.precio, pedidos[0].precio) },
            { assertEquals(pedidoTest.usuario, pedidos[0].usuario) },
        )
    }

    @Test
    fun encontrarPedidoID(): Unit = runBlocking  {
        val pedidoId = controlador.encontrarPedidoID(pedidoTest.id)!!
        assertAll(
            { assertEquals(pedidoTest.id, pedidoId.id) },
            { assertEquals(pedidoTest.uuidPedidos, pedidoId.uuidPedidos) },
            { assertEquals(pedidoTest.estado, pedidoId.estado) },
            { assertEquals(pedidoTest.fechaEntrada, pedidoId.fechaEntrada) },
            { assertEquals(pedidoTest.fechaSalidaProgramada, pedidoId.fechaSalidaProgramada) },
            { assertEquals(pedidoTest.fechaEntrega, pedidoId.fechaEntrega) },
            { assertEquals(pedidoTest.precio, pedidoId.precio) },
            { assertEquals(pedidoTest.usuario, pedidoId.usuario) },
        )

    }

    @Test
    fun encontrarPedidoUUID(): Unit = runBlocking  {
        val pedidoUuid = controlador.encontrarPedidoUUID(pedidoTest.uuidPedidos)!!
        assertAll(
            { assertEquals(pedidoTest.id, pedidoUuid.id) },
            { assertEquals(pedidoTest.uuidPedidos, pedidoUuid.uuidPedidos) },
            { assertEquals(pedidoTest.estado, pedidoUuid.estado) },
            { assertEquals(pedidoTest.fechaEntrada, pedidoUuid.fechaEntrada) },
            { assertEquals(pedidoTest.fechaSalidaProgramada, pedidoUuid.fechaSalidaProgramada) },
            { assertEquals(pedidoTest.fechaEntrega, pedidoUuid.fechaEntrega) },
            { assertEquals(pedidoTest.precio, pedidoUuid.precio) },
            { assertEquals(pedidoTest.usuario, pedidoUuid.usuario) },
        )
    }

    @Test
    fun guardarPedido(): Unit = runBlocking  {
        controlador.borrarPedido(pedidoTest)
        val pedidoId = controlador.guardarPedido(pedidoTest)!!
        assertAll(
            { assertEquals(pedidoTest.id, pedidoId.id) },
            { assertEquals(pedidoTest.uuidPedidos, pedidoId.uuidPedidos) },
            { assertEquals(pedidoTest.estado, pedidoId.estado) },
            { assertEquals(pedidoTest.fechaEntrada, pedidoId.fechaEntrada) },
            { assertEquals(pedidoTest.fechaSalidaProgramada, pedidoId.fechaSalidaProgramada) },
            { assertEquals(pedidoTest.fechaEntrega, pedidoId.fechaEntrega) },
            { assertEquals(pedidoTest.precio, pedidoId.precio) },
            { assertEquals(pedidoTest.usuario, pedidoId.usuario) },
        )

    }

    @Test
    fun borrarPedido(): Unit = runBlocking  {
        val pedidoDelete = controlador.borrarPedido(pedidoTest)
        assertAll(
            { assertTrue(pedidoDelete) }
        )
    }

    @Test
    fun listarProductos(): Unit = runBlocking  {
        val productos = controlador.listarProductos().toList()
        assertAll(
            { assertFalse(productos.isEmpty()) },
            { assertEquals(productos.first().uuidProducto, productoTest.uuidProducto) },
            { assertEquals(productos.first().marca, productoTest.marca) },
            { assertEquals(productos.first().modelo, productoTest.modelo) },
            { assertEquals(productos.first().precio, productoTest.precio) },
            { assertEquals(productos.first().stock, productoTest.stock) },
            { assertEquals(productos.size, 1) }
        )
    }

    @Test
    fun encontrarProductoID(): Unit = runBlocking  {
        val testID = controlador.encontrarProductoID(productoTest.id)
        assertAll(
            { assertEquals(testID!!.uuidProducto, productoTest.uuidProducto) },
            { assertEquals(testID!!.marca, productoTest.marca) },
            { assertEquals(testID!!.modelo, productoTest.modelo) },
            { assertEquals(testID!!.precio, productoTest.precio) },
            { assertEquals(testID!!.stock, productoTest.stock) },
        )
    }

    @Test
    fun encontrarProductoUUID(): Unit = runBlocking  {
        val testUUID = controlador.encontrarProductoUUID(productoTest.uuidProducto)
        assertAll(
            { assertEquals(testUUID!!.uuidProducto, productoTest.uuidProducto) },
            { assertEquals(testUUID!!.marca, productoTest.marca) },
            { assertEquals(testUUID!!.modelo, productoTest.modelo) },
            { assertEquals(testUUID!!.precio, productoTest.precio) },
            { assertEquals(testUUID!!.stock, productoTest.stock) },
        )
    }

    @Test
    fun guardarProducto(): Unit = runBlocking  {
        val testSave = controlador.guardarProducto(productoTest)
        assertAll(
            { assertEquals(testSave!!.uuidProducto, productoTest.uuidProducto) },
            { assertEquals(testSave!!.marca, productoTest.marca) },
            { assertEquals(testSave!!.modelo, productoTest.modelo) },
            { assertEquals(testSave!!.precio, productoTest.precio) },
            { assertEquals(testSave!!.stock, productoTest.stock) },
        )
    }

    @Test
    fun borrarProducto(): Unit = runBlocking  {
        val testDelete = controlador.borrarProducto(productoTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }

    @Test
    fun listarTareas(): Unit = runBlocking  {
        val test = controlador.listarTareas().toList()
        assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(test.first().uuidTarea, tareaTest.uuidTarea) },
            { assertEquals(test.first().producto, tareaTest.producto) },
            { assertEquals(test.first().precio, tareaTest.precio) },
            { assertEquals(test.first().descripcion, tareaTest.descripcion) },
            { assertEquals(test.first().empleado, tareaTest.empleado) },
            { assertEquals(test.first().turno, tareaTest.turno) },
            { assertEquals(test.first().estadoCompletado, tareaTest.estadoCompletado) },
            { assertEquals(test.first().maquina, tareaTest.maquina) },
            { assertEquals(test.first().pedido, tareaTest.pedido) },
            { assertEquals(test.size, 1) }
        )
    }

    @Test
    fun encontrarTareaID(): Unit = runBlocking  {
        val testID = controlador.encontrarTareaID(tareaTest.id)
        assertAll(
            { assertEquals(testID!!.uuidTarea, tareaTest.uuidTarea) },
            { assertEquals(testID!!.producto, tareaTest.producto) },
            { assertEquals(testID!!.precio, tareaTest.precio) },
            { assertEquals(testID!!.descripcion, tareaTest.descripcion) },
            { assertEquals(testID!!.empleado, tareaTest.empleado) },
            { assertEquals(testID!!.turno, tareaTest.turno) },
            { assertEquals(testID!!.estadoCompletado, tareaTest.estadoCompletado) },
            { assertEquals(testID!!.maquina, tareaTest.maquina) },
            { assertEquals(testID!!.pedido, tareaTest.pedido) },
        )
    }

    @Test
    fun encontrarTareaUUID(): Unit = runBlocking  {
        val testUUID = controlador.encontrarTareaUUID(tareaTest.uuidTarea)
        assertAll(
            { assertEquals(testUUID!!.uuidTarea, tareaTest.uuidTarea) },
            { assertEquals(testUUID!!.producto, tareaTest.producto) },
            { assertEquals(testUUID!!.precio, tareaTest.precio) },
            { assertEquals(testUUID!!.descripcion, tareaTest.descripcion) },
            { assertEquals(testUUID!!.empleado, tareaTest.empleado) },
            { assertEquals(testUUID!!.turno, tareaTest.turno) },
            { assertEquals(testUUID!!.estadoCompletado, tareaTest.estadoCompletado) },
            { assertEquals(testUUID!!.maquina, tareaTest.maquina) },
            { assertEquals(testUUID!!.pedido, tareaTest.pedido) },
        )
    }

    @Test
    fun guardarTarea(): Unit = runBlocking  {
        val testSave = controlador.guardarTarea(tareaTest)
        assertAll(
            { assertEquals(testSave!!.uuidTarea, tareaTest.uuidTarea) },
            { assertEquals(testSave!!.producto, tareaTest.producto) },
            { assertEquals(testSave!!.precio, tareaTest.precio) },
            { assertEquals(testSave!!.descripcion, tareaTest.descripcion) },
            { assertEquals(testSave!!.empleado, tareaTest.empleado) },
            { assertEquals(testSave!!.turno, tareaTest.turno) },
            { assertEquals(testSave!!.estadoCompletado, tareaTest.estadoCompletado) },
            { assertEquals(testSave!!.maquina, tareaTest.maquina) },
            { assertEquals(testSave!!.pedido, tareaTest.pedido) },
        )
    }

    @Test
    fun borrarTarea(): Unit = runBlocking  {
        val testDelete = controlador.borrarTarea(tareaTest)
        assertAll(
            { assertTrue(testDelete) }
        )
    }

    @Test
    fun listarUsuarios(): Unit = runBlocking  {
        val test = controlador.listarUsuarios().toList()
        assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(test.first().uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(test.first().nombre, usuarioTest.nombre) },
            { assertEquals(test.first().apellido, usuarioTest.apellido) },
            { assertEquals(test.first().email, usuarioTest.email) },
            { assertEquals(1, test.size) }
        )
    }

    @Test
    fun encontrarUsuarioID(): Unit = runBlocking  {
        val testId = controlador.encontrarUsuarioID(usuarioTest.id)
        assertAll(
            { assertEquals(testId!!.uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(testId!!.nombre, usuarioTest.nombre) },
            { assertEquals(testId!!.apellido, usuarioTest.apellido) },
            { assertEquals(testId!!.email, usuarioTest.email) },
        )
    }

    @Test
    fun encontrarUsuarioUUID(): Unit = runBlocking  {
        val testUUId = controlador.encontrarUsuarioUUID(usuarioTest.uuidUsuario)
        assertAll(
            { assertEquals(testUUId!!.uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(testUUId!!.nombre, usuarioTest.nombre) },
            { assertEquals(testUUId!!.apellido, usuarioTest.apellido) },
            { assertEquals(testUUId!!.email, usuarioTest.email) },
        )
    }

    @Test
    fun guardarUsuario(): Unit = runBlocking  {
        SqlDeLightClient.queries.deleteAll()
        val testSave = controlador.guardarUsuario(usuarioTest)!!
        assertAll(
            { assertEquals(testSave.uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(testSave.nombre, usuarioTest.nombre) },
            { assertEquals(testSave.apellido, usuarioTest.apellido) },
            { assertEquals(testSave.email, usuarioTest.email) },
        )
    }

    @Test
    fun borrarUsuario(): Unit = runBlocking  {
        val testDelete = controlador.borrarUsuario(usuarioTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }

    @Test
    fun listarTurnos(): Unit = runBlocking  {
        val test = controlador.listarTurnos().toList()
        assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(test.first().uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(test.first().fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(test.first().fechaFin, turnoTest.fechaFin) },
            { assertEquals(test.size, 1) }
        )
    }

    @Test
    fun encontrarTurnoID(): Unit = runBlocking  {
        val testId = controlador.encontrarTurnoID(turnoTest.id)
        assertAll(
            { assertEquals(testId!!.uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(testId!!.fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(testId!!.fechaFin, turnoTest.fechaFin) }
        )
    }

    @Test
    fun encontrarTurnoUUID(): Unit = runBlocking  {
        val testUUId = controlador.encontrarTurnoUUID(turnoTest.uuidTurno)
        assertAll(
            { assertEquals(testUUId!!.uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(testUUId!!.fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(testUUId!!.fechaFin, turnoTest.fechaFin) }
        )
    }

    @Test
    fun guardarTurno(): Unit = runBlocking  {
        val testSave = controlador.guardarTurno(turnoTest)!!
        assertAll(
            { assertEquals(testSave.uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(testSave.fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(testSave.fechaFin, turnoTest.fechaFin) }
        )
    }

    @Test
    fun borrarTurno(): Unit = runBlocking  {
        val testDelete = controlador.borrarTurno(turnoTest)
        assertAll(
            { assertTrue(testDelete) }
        )
    }

    @Test
    fun encontrarUsuariosAPI(): Unit = runBlocking  {
        val test = controlador.encontrarUsuariosAPI().toList()
        assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(10, test.size) },
            { assertEquals("Leanne Graham", test[0].nombre) },
            { assertEquals("Bret", test[0].apellido) },
            { assertEquals("Sincere@april.biz", test[0].email) },
            { assertEquals(null, test[0].turno) },
            { assertEquals(null, test[0].pedido) },
        )
    }

    @Test
    fun guardarUsuariosAPI(): Unit = runBlocking  {
        val testSave = controlador.guardarUsuariosAPI(usuarioTest)
        assertAll(
            { assertEquals(usuarioTest.nombre, testSave.nombre) },
            { assertEquals(usuarioTest.apellido, testSave.apellido) },
            { assertEquals(usuarioTest.email, testSave.email) },
            { assertEquals(usuarioTest.turno, testSave.turno) },
            { assertEquals(usuarioTest.pedido, testSave.pedido) },
        )
    }

}