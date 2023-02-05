package repositories.usuario

import db.MongoDbManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Contextual
import models.Usuario
import models.enums.TipoPerfil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.litote.kmongo.newId
import repositories.turno.TurnoRepositoryImpl
import services.password.Password
import services.sqldelight.SqlDeLightClient
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioRepositoryImplTest {


    val dataBaseService = MongoDbManager.database
    val repositorio = UsuarioRepositoryImpl()
    @Contextual
    val usuarioTest =  Usuario(
        newId(),
        "492a7f86-c34d-43e3-ba77-8083a542f427",
        "Mario",
        "Sánchez",
        "mario.sanchez@gmail.com",
        Password().encriptar("marioSanchez"),
        TipoPerfil.ENCORDADOR,
        null,
        null
    )

    @BeforeEach
    fun setUp(): Unit = runTest {
        dataBaseService.drop()
        SqlDeLightClient.queries.deleteAll()
        repositorio.save(usuarioTest)
    }

    @Test
    fun findAll(): Unit = runTest {
        val test = repositorio.findAll().toList()
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
    fun findById(): Unit = runTest {
        val testId = repositorio.findById(usuarioTest.id)
        assertAll(
            { assertEquals(testId!!.uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(testId!!.nombre, usuarioTest.nombre) },
            { assertEquals(testId!!.apellido, usuarioTest.apellido) },
            { assertEquals(testId!!.email, usuarioTest.email) },
        )
    }

    @Test
    fun findbyUUID(): Unit = runTest {
        val testUUId = repositorio.findByUUID(usuarioTest.uuidUsuario)
        assertAll(
            { assertEquals(testUUId!!.uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(testUUId!!.nombre, usuarioTest.nombre) },
            { assertEquals(testUUId!!.apellido, usuarioTest.apellido) },
            { assertEquals(testUUId!!.email, usuarioTest.email) },
        )
    }


    @Test
    fun save(): Unit = runTest {
        SqlDeLightClient.queries.deleteAll()
        val testSave = repositorio.save(usuarioTest)!!
        assertAll(
            { assertEquals(testSave.uuidUsuario, usuarioTest.uuidUsuario) },
            { assertEquals(testSave.nombre, usuarioTest.nombre) },
            { assertEquals(testSave.apellido, usuarioTest.apellido) },
            { assertEquals(testSave.email, usuarioTest.email) },
        )
    }

    @Test
    fun delete(): Unit = runTest {
        val testDelete = repositorio.delete(usuarioTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }
}