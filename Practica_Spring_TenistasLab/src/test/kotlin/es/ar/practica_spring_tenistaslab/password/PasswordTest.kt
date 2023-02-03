package services.password

import es.ar.practica_spring_tenistaslab.services.password.Password
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PasswordTest {
    val cadenaOriginal = "TestMongo"
    val cadenaEncriptada = Password().encriptar(cadenaOriginal)

    @Test
    fun encriptar() {
        val cadenaOriginal = "TestMongo"
        val cadenaTest = Password().encriptar("TestMongo")
        val cadenaTestVerify = Password().verificar(cadenaOriginal, cadenaTest)

        assertAll(
            { assertTrue(cadenaTestVerify) }
        )
    }

    @Test
    fun verificar() {
        val cadenaTest = Password().verificar(cadenaOriginal, cadenaEncriptada)

        assertAll(
            { assertTrue(cadenaTest) }
        )
    }
}