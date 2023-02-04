package repositories.maquina

import db.MongoDbManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun setUp(): Unit = runTest {
        dataBaseService.drop()
        repository.save(maquinaTest)

    }

    @Test
    fun findAll(): Unit = runTest {
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



    @Test
    fun findById(): Unit = runTest  {
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


    @Test
    fun findbyUUID(): Unit = runTest  {
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


    @Test
    fun save(): Unit = runTest  {
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

    @Test
    fun delete(): Unit = runTest  {
        val testDelete = repository.delete(maquinaTest)
        assertAll(
            { assertTrue(testDelete) },
        )
    }
}