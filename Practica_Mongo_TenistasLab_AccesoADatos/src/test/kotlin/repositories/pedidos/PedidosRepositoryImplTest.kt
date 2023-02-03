package repositories.pedidos

import db.MongoDbManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mapper.toUsuarioDto
import models.*
import models.enums.TipoEstado
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.litote.kmongo.id.toId
import repositories.maquina.MaquinaRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
import repositories.tarea.TareasRepositoryImpl
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.UsuarioRepositoryImpl
import services.password.Password
import services.sqldelight.SqlDeLightClient
import java.time.LocalDate
import java.time.LocalDateTime

class PedidosRepositoryImplTest {

    val dataBaseService = MongoDbManager.database
    val repositorio = PedidosRepositoryImpl()

    val preparacion = runBlocking {
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
            TipoPerfil.ENCORDADOR,
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
    }

    val pedidoTest = runBlocking {
        repositorio.findByUUID("492a7f86-c34d-43e3-ba77-8083a542f425")!!
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
            TipoPerfil.ENCORDADOR,
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



    }


    @Test
    fun findAll(): Unit = runBlocking {
        val pedidos = repositorio.findAll().toList()
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
    fun findById(): Unit = runBlocking {
        val pedidoId = repositorio.findById(pedidoTest.id)!!
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
    fun findByUUID(): Unit = runBlocking{
        val pedidoUuid = repositorio.findByUUID(pedidoTest.uuidPedidos)!!
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
    fun save(): Unit = runBlocking {
        repositorio.delete(pedidoTest)
        val pedidoId = repositorio.save(pedidoTest)!!
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
    fun delete(): Unit = runBlocking {
        val pedidoDelete = repositorio.delete(pedidoTest)
        assertAll(
            { assertTrue(pedidoDelete) }
        )
    }
}