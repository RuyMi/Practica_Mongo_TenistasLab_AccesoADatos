package services.password

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class PasswordTest {
    val cadenaOriginal = "TestMongo"
    val cadenaEncriptada = Password().encriptar(cadenaOriginal)

    @Test
    fun encriptar() = runTest {
        val cadenaOriginal = "TestMongo"
        val cadenaTest = Password().encriptar("TestMongo")
        val cadenaTestVerify = Password().verificar(cadenaOriginal, cadenaTest)

        assertAll(
            { assertTrue(cadenaTestVerify) }
        )
    }


    @Test
    fun verificar() = runTest {
        val cadenaTest = Password().verificar(cadenaOriginal, cadenaEncriptada)

        assertAll(
            { assertTrue(cadenaTest) }
        )
    }
}