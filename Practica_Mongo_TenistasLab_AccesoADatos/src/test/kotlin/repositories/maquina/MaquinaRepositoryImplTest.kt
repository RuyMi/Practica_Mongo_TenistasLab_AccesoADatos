package repositories.maquina

import db.MongoDbManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import models.Maquina
import models.Usuario
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import org.litote.kmongo.newId
import services.password.Password
import java.time.LocalDate

class MaquinaRepositoryImplTest {

    val dataBaseService = MongoDbManager.database
    val repository = MaquinaRepositoryImpl()
    val maquinaTest = Maquina(
        newId(),
        "a016f77a-4698-4bd3-8294-1edb74311d27",
        "nadal",
        "rojo",
        LocalDate.now(),
        "true, 20.0, 12.4",
        TipoMaquina.ENCORDAR
    )

    @BeforeEach
    fun setUp(): Unit = runBlocking {
        dataBaseService.drop()
        repository.save(maquinaTest)

    }


    @Order(1)
    @Test
    fun findAll(): Unit = runBlocking {
        val maquina = repository.findAll().toList()
        assertAll(
            {assertEquals(1, maquina.size)},
            { assertFalse(maquina.isEmpty()) },
            {assertEquals(maquinaTest.id, maquina[0].id)},
            {assertEquals(maquinaTest.numSerie, maquina[0].numSerie)},
            {assertEquals(maquinaTest.descripcion, maquina[0].descripcion)},
            {assertEquals(maquinaTest.marca, maquina[0].marca)},
            {assertEquals(maquinaTest.fechaAdquisicion, maquina[0].fechaAdquisicion)},
            {assertEquals(maquinaTest.modelo, maquina[0].modelo)},
            {assertEquals(maquinaTest.tipo, maquina[0].tipo)},
        )

    }


    @Order(2)
    @Test
    fun findById(): Unit = runBlocking  {
        val testID = repository.findById(maquinaTest.id)
        assertAll(
            { assertEquals(testID!!.id, maquinaTest.id) },
            { assertEquals(testID!!.numSerie, maquinaTest.numSerie) },
            { assertEquals(testID!!.marca, maquinaTest.marca) },
            { assertEquals(testID!!.modelo, maquinaTest.modelo) },
            { assertEquals(testID!!.descripcion, maquinaTest.descripcion) },
            { assertEquals(testID!!.fechaAdquisicion, maquinaTest.fechaAdquisicion) },
        )
    }

    @Order(3)
    @Test
    fun findbyUUID(): Unit = runBlocking  {
        val testUUID = repository.findByUUID(maquinaTest.numSerie)
        assertAll(
            { assertEquals(testUUID!!.id, maquinaTest.id) },
            { assertEquals(testUUID!!.numSerie, maquinaTest.numSerie) },
            { assertEquals(testUUID!!.marca, maquinaTest.marca) },
            { assertEquals(testUUID!!.modelo, maquinaTest.modelo) },
            { assertEquals(testUUID!!.descripcion, maquinaTest.descripcion) },
            { assertEquals(testUUID!!.fechaAdquisicion, maquinaTest.fechaAdquisicion) },
        )
    }

    @Order(4)
    @Test
    fun save(): Unit = runBlocking  {
        val testSave = repository.save(maquinaTest)
        assertAll(
            { assertEquals(testSave!!.id, maquinaTest.id) },
            { assertEquals(testSave!!.numSerie, maquinaTest.numSerie) },
            { assertEquals(testSave!!.marca, maquinaTest.marca) },
            { assertEquals(testSave!!.modelo, maquinaTest.modelo) },
            { assertEquals(testSave!!.descripcion, maquinaTest.descripcion) },
            { assertEquals(testSave!!.fechaAdquisicion, maquinaTest.fechaAdquisicion) },
        )
    }

    @Order(5)
    @Test
    fun delete(): Unit = runBlocking  {
        val testDelete = repository.delete(maquinaTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }
}