package repositories.producto

import db.MongoDbManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import models.Producto
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.litote.kmongo.newId
import java.util.*
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoRepositoryImplTest {

    val dataBaseService = MongoDbManager.database
    val repositorio = ProductoRepositoryImpl()
    val productoTest = Producto(
        newId(),
        "492a7f86-c34d-43e3-ba77-8083a542f427",
        "Wilson",
        "raqueta",
        20.2,
        12
    )

    @BeforeEach
    fun setUp(): Unit = runTest {
        dataBaseService.drop()
        repositorio.save(productoTest)

    }

    @Test
    fun findAll(): Unit = runTest {
        val test = repositorio.findAll().toList()
        assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(test.first().uuidProducto, productoTest.uuidProducto) },
            { assertEquals(test.first().marca, productoTest.marca) },
            { assertEquals(test.first().modelo, productoTest.modelo) },
            { assertEquals(test.first().precio, productoTest.precio) },
            { assertEquals(test.first().stock, productoTest.stock) },
            { assertEquals(test.size, 1) }
        )
    }


    @Test
    fun findById(): Unit = runTest {
        val testID = repositorio.findById(productoTest.id)
        assertAll(
            { assertEquals(testID!!.uuidProducto, productoTest.uuidProducto) },
            { assertEquals(testID!!.marca, productoTest.marca) },
            { assertEquals(testID!!.modelo, productoTest.modelo) },
            { assertEquals(testID!!.precio, productoTest.precio) },
            { assertEquals(testID!!.stock, productoTest.stock) },
        )
    }


    @Test
    fun findbyUUID(): Unit = runTest {
        val testUUID = repositorio.findByUUID(productoTest.uuidProducto)
        assertAll(
            { assertEquals(testUUID!!.uuidProducto, productoTest.uuidProducto) },
            { assertEquals(testUUID!!.marca, productoTest.marca) },
            { assertEquals(testUUID!!.modelo, productoTest.modelo) },
            { assertEquals(testUUID!!.precio, productoTest.precio) },
            { assertEquals(testUUID!!.stock, productoTest.stock) },
        )
    }


    @Test
    fun save(): Unit = runTest {
        val testSave = repositorio.save(productoTest)
        assertAll(
            { assertEquals(testSave!!.uuidProducto, productoTest.uuidProducto) },
            { assertEquals(testSave!!.marca, productoTest.marca) },
            { assertEquals(testSave!!.modelo, productoTest.modelo) },
            { assertEquals(testSave!!.precio, productoTest.precio) },
            { assertEquals(testSave!!.stock, productoTest.stock) },
        )
    }


    @Test
    fun delete(): Unit = runTest {
        val testDelete = repositorio.delete(productoTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }
}