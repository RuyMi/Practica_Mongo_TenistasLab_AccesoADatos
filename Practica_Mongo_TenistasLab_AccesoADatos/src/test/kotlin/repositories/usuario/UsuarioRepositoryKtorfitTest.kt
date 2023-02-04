package repositories.usuario

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.serialization.Contextual
import models.Usuario
import models.enums.TipoPerfil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.litote.kmongo.newId
import services.password.Password

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioRepositoryKtorfitTest {

    val repositorio = UsuarioRepositoryKtorfit()

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


    @Test
    fun findAll()= runTest {
        val test = repositorio.findAll().toList()
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
    fun findById()= runTest {
        val testId = repositorio.findById(1)
        assertAll(
            { assertEquals("Leanne Graham", testId!!.nombre) },
            { assertEquals("Bret", testId!!.apellido) },
            { assertEquals("Sincere@april.biz", testId!!.email) },
            { assertEquals(null, testId!!.turno) },
            { assertEquals(null, testId!!.pedido) },
        )

    }

    @Test
    fun save()= runTest {
        val testSave = repositorio.save(usuarioTest)
        assertAll(
            { assertEquals(usuarioTest.nombre, testSave.nombre) },
            { assertEquals(usuarioTest.apellido, testSave.apellido) },
            { assertEquals(usuarioTest.email, testSave.email) },
            { assertEquals(usuarioTest.turno, testSave.turno) },
            { assertEquals(usuarioTest.pedido, testSave.pedido) },
        )
    }


    @Test
    fun delete()= runTest {
        val usuarioDelete = repositorio.delete(usuarioTest)
        assertAll(
            { assertEquals(usuarioTest.nombre, usuarioDelete.nombre) },
            { assertEquals(usuarioTest.apellido, usuarioDelete.apellido) },
            { assertEquals(usuarioTest.email, usuarioDelete.email) },
            { assertEquals(usuarioTest.turno, usuarioDelete.turno) },
            { assertEquals(usuarioTest.pedido, usuarioDelete.pedido) },
        )
    }
}