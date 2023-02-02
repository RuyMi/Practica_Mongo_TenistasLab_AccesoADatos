package repositories.turno

import db.MongoDbManager
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import models.Turno
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.litote.kmongo.newId
import repositories.maquina.MaquinaRepositoryImpl
import java.time.LocalDateTime
import java.util.*

class TurnoRepositoryImplTest {


    val dataBaseService = MongoDbManager.database
    val repositorio = TurnoRepositoryImpl()
    val turnoTest =
        Turno(
            newId(),
            "492a7f86-c34d-43e3-ba77-8083a542f426",
            LocalDateTime.of(2022, 12, 5, 8, 0),
            LocalDateTime.of(2022, 12, 5, 10, 0)
        )

    @BeforeEach
    fun setUp(): Unit = runBlocking {
        dataBaseService.drop()
        repositorio.save(turnoTest)
    }



    @Test
    fun findAll(): Unit = runBlocking {
        val test = repositorio.findAll().toList()
        assertAll(
            { assertFalse(test.isEmpty()) },
            { assertEquals(test.first().uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(test.first().fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(test.first().fechaFin, turnoTest.fechaFin) },
            { assertEquals(test.size, 1) }
        )
    }


    @Test
    fun findById(): Unit = runBlocking {
        val testId = repositorio.findById(turnoTest.id)
        assertAll(
            { assertEquals(testId!!.uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(testId!!.fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(testId!!.fechaFin, turnoTest.fechaFin) }
        )
    }


    @Test
    fun findbyUUID(): Unit = runBlocking {
        val testUUId = repositorio.findByUUID(turnoTest.uuidTurno)
        assertAll(
            { assertEquals(testUUId!!.uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(testUUId!!.fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(testUUId!!.fechaFin, turnoTest.fechaFin) }
        )
    }


    @Test
    fun save(): Unit = runBlocking {
        val testSave = repositorio.save(turnoTest)!!
        assertAll(
            { assertEquals(testSave.uuidTurno, turnoTest.uuidTurno) },
            { assertEquals(testSave.fechaInicio, turnoTest.fechaInicio) },
            { assertEquals(testSave.fechaFin, turnoTest.fechaFin) }
        )
    }


    @Test
    fun delete(): Unit = runBlocking {
        val testDelete = repositorio.delete(turnoTest)
        assertAll(
            { assertTrue(testDelete) }
        )
    }
}