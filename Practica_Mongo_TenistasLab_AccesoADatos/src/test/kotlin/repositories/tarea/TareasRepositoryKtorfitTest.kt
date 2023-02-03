package repositories.tarea

import kotlinx.coroutines.runBlocking
import mapper.toUsuarioDto
import models.*
import models.enums.TipoEstado
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.UsuarioRepositoryImpl
import services.password.Password
import java.time.LocalDate
import java.time.LocalDateTime

class TareasRepositoryKtorfitTest {

    val tareaTest = Tarea(
        ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
        "492a7f86-c34d-4313-ba77-8083a542f425",
        Producto(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-43e3-ba77-8183a542f425",
            "Wilson",
            "raqueta",
            20.2,
            12
        ),
        20.2,
        "Personalizacion",
        Usuario(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c32d-43e3-ba77-8083a542f425",
            "Mario",
            "Sánchez",
            "mario.sanchez@gmail.com",
            Password().encriptar("marioSanchez"),
            TipoPerfil.ENCORDADOR,
            Turno(
                ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
                "492a7f86-c34d-4313-ba77-8083a542f425",
                LocalDateTime.of(2022, 12, 5, 8, 0),
                LocalDateTime.of(2022, 12, 5, 10, 0)
            ).id,
            null
        ).toUsuarioDto(),
        Turno(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-4313-ba77-8083a542f425",
            LocalDateTime.of(2022, 12, 5, 8, 0),
            LocalDateTime.of(2022, 12, 5, 10, 0)
        ),
        true,
        Maquina(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "a016f77a-4698-4bd3-8294-1edb74311d27",
            "nadal",
            "rojo",
            LocalDate.now(),
            "true, 20.0, 12.4",
            TipoMaquina.ENCORDAR
        ),
        Pedidos(
            ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-43e3-ba77-8083a542f425",
            TipoEstado.RECIBIDO,
            LocalDate.now(),
            LocalDate.of(2022, 12, 12),
            LocalDate.of(2022, 12, 13),
            120.5,
            Usuario(
                ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
                "492a7f86-c32d-43e3-ba77-8083a542f425",
                "Mario",
                "Sánchez",
                "mario.sanchez@gmail.com",
                Password().encriptar("marioSanchez"),
                TipoPerfil.ENCORDADOR,
                Turno(ObjectId("63dc3ad64f5c531dfe3c3795").toId(),
            "492a7f86-c34d-4313-ba77-8083a542f425",
                LocalDateTime.of(2022, 12, 5, 8, 0),
                LocalDateTime.of(2022, 12, 5, 10, 0)
                ).id,
                null
            ).id
        )
    )

    val repo = TareasRepositoryKtorfit()

    @Test
    fun save(): Unit = runBlocking {
        val tareaSave = repo.save(tareaTest)
        assertAll(
            {  assertEquals(tareaSave.uuidTarea, tareaTest.uuidTarea) },
            { assertEquals(tareaSave.producto, tareaTest.producto) },
            { assertEquals(tareaSave.precio, tareaTest.precio) },
            { assertEquals(tareaSave.descripcion, tareaTest.descripcion) },
            { assertEquals(tareaSave.empleado, tareaTest.empleado) },
            { assertEquals(tareaSave.turno, tareaTest.turno) },
            { assertEquals(tareaSave.estadoCompletado, tareaTest.estadoCompletado) },
            { assertEquals(tareaSave.maquina, tareaTest.maquina) },
            { assertEquals(tareaSave.pedido, tareaTest.pedido) },
        )
    }
}