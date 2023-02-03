package repositories.tarea

import db.MongoDbManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mapper.toUsuarioDto
import models.*
import models.enums.TipoEstado
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.UsuarioRepositoryImpl
import services.password.Password
import services.sqldelight.SqlDeLightClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class TareasRepositoryImplTest {

    val dataBaseService = MongoDbManager.database
    val repositorio = TareasRepositoryImpl()

    val tareaTest = runBlocking {
        repositorio.findByUUID("192a7f86-c34d-43e3-ba77-8083a542f425")
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
            )}
        repositorio.save(tareaTest)

    }


    @Test
    fun findAll(): Unit = runBlocking  {
        val test = repositorio.findAll().toList()
        Assertions.assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(test.first().uuidTarea, tareaTest!!.uuidTarea) },
            { assertEquals(test.first().producto, tareaTest!!.producto) },
            { assertEquals(test.first().precio, tareaTest!!.precio) },
            { assertEquals(test.first().descripcion, tareaTest!!.descripcion) },
            { assertEquals(test.first().empleado, tareaTest!!.empleado) },
            { assertEquals(test.first().turno, tareaTest!!.turno) },
            { assertEquals(test.first().estadoCompletado, tareaTest!!.estadoCompletado) },
            { assertEquals(test.first().maquina, tareaTest!!.maquina) },
            { assertEquals(test.first().pedido, tareaTest!!.pedido) },
            { assertEquals(test.size, 1) }
        )
    }


    @Test
    fun findById(): Unit = runBlocking {
        val testID = repositorio.findById(tareaTest!!.id)
        Assertions.assertAll(
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
    fun findbyUUID(): Unit = runBlocking {
        val testUUID = repositorio.findByUUID(tareaTest!!.uuidTarea)
        Assertions.assertAll(
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
    fun save(): Unit = runBlocking {
        val testSave = repositorio.save(tareaTest!!)
        Assertions.assertAll(
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
    fun delete(): Unit = runBlocking {
        val testDelete = tareaTest?.let { repositorio.delete(it) }
        Assertions.assertAll(
            {
                if (testDelete != null) {
                    assertTrue(testDelete)
                }
            }
        )
    }


    companion object {
        @JvmStatic
        @BeforeAll
        fun setUpAll(): Unit = runBlocking{
            MongoDbManager.database.drop()
        }
    }


}