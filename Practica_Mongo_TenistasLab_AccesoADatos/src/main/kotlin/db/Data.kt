package db


import models.*
import models.enums.TipoEstado
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.litote.kmongo.newId
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.UsuarioRepositoryImpl
import services.password.Password
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun getTurnos(): List<Turno> {
    return listOf(
        Turno(
            newId(),
            UUID.fromString("5f6cab09-31a4-46d0-8d69-e4e510d2a227"),
            LocalDateTime.of(2022, 12, 5, 8, 0),
            LocalDateTime.of(2022, 12, 5, 10, 0)
        ),
        Turno(
            newId(),
            UUID.fromString("ee56f791-f545-466f-959f-4683d558c692"),
            LocalDateTime.of(2022, 12, 5, 10, 0),
            LocalDateTime.of(2022, 12, 5, 12, 0)
        ),
        Turno(
            newId(),
            UUID.fromString("b4023b8f-68a1-4d93-8519-8d0b54f7013d"),
            LocalDateTime.of(2022, 12, 5, 12, 0),
            LocalDateTime.of(2022, 12, 5, 14, 0)
        ),
        Turno(
            newId(),
            UUID.fromString("88a03bc5-9cde-40a9-9087-883b74339e75"),
            LocalDateTime.of(2022, 12, 5, 14, 0),
            LocalDateTime.of(2022, 12, 5, 16, 0)
        )
    )
}

suspend fun getUsuarios(): List<Usuario?> {
return listOf(
    TurnoRepositoryImpl().findByUUID(UUID.fromString("5f6cab09-31a4-46d0-8d69-e4e510d2a227"))?.let {
        Usuario(
            newId(),
            UUID.fromString("5e187396-a777-4a07-871d-90ed04e1af8a"),
            "Mario",
            "Sánchez",
            "mario.sanchez@gmail.com",
            Password().encriptar("marioSanchez"),
            TipoPerfil.ENCORDADOR,
            it.id,
            null
        )
    },
    Usuario(
        newId(),
        UUID.fromString("4747bf2d-22dd-4a29-89c8-c1a7492769ae"),
        "Andrés",
        "Márquez",
        "andres.marquez@gmail.com",
        Password().encriptar("andresMarquez"),
        TipoPerfil.USUARIO,
        null,
        null
    ),
    TurnoRepositoryImpl().findByUUID(UUID.fromString("ee56f791-f545-466f-959f-4683d558c692"))?.let {
        Usuario(
            newId(),
            UUID.fromString("38ac290a-b3d5-4ef6-a9ab-8f24df5724f1"),
            "Rubén",
            "García-Redondo",
            "rubengrm@gmail.com",
            Password().encriptar("rubengrm"),
            TipoPerfil.ENCORDADOR,
            it.id,
            null
        )
    },
    TurnoRepositoryImpl().findByUUID(UUID.fromString("b4023b8f-68a1-4d93-8519-8d0b54f7013d"))?.let {
        Usuario(
            newId(),
            UUID.fromString("2c172996-ce30-470a-b796-7b03e6224055"),
            "Álvaro",
            "Yubero",
            "alvaro.yubero@gmail.com",
            Password().encriptar("alvaroYubero"),
            TipoPerfil.ENCORDADOR,
            it.id,
            null
        )
    }
)
}

fun getMaquinasEncordar(): List<Maquina> {
    return listOf(
        Maquina(
            newId(),
            UUID.fromString("a016f77a-4698-4bd3-8294-1edb74311d27"),
            "nadal",
            "rojo",
            LocalDate.now(),
            "true, 20.0, 12.4",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            UUID.fromString("16479c98-4d85-44ed-b3a2-67b8c455024c"),
            "nike",
            "verde",
            LocalDate.now(),
            "true,19.2, 10.4",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            UUID.fromString("a1ec9a97-639f-4652-8dcb-4ef256ca96ab"),
            "Adidas",
            "Azul",
            LocalDate.now(),
            "false, 22.0, 13.1,",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            UUID.fromString("381ffc54-a2fe-47d9-ab45-04d9b21b293b"),
            "Apple",
            "morado",
            LocalDate.now(),
            """true,
            25.0,
            9.1""",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            UUID.fromString("00339a54-6eb4-4a49-820c-dc49183a564a"),
            "perso1",
            "rojo",
            LocalDate.now(),
            "true, 1.0, 10.4",
            TipoMaquina.PERSONALIZACION
        ),
        Maquina(
            newId(),
            UUID.fromString("02c6bb52-ba65-41a9-8458-850d284bab07"),
            "nike",
            "verde",
            LocalDate.now(),
            "true, 19.2, 10.4",
            TipoMaquina.PERSONALIZACION
        ),
        Maquina(
            newId(),
            UUID.fromString("5e8a23a4-401f-4d3d-9f33-197419af57d9"),
            "Adidas",
            "Azul",
            LocalDate.now(),
            "false, 22.0, 13.1",
            TipoMaquina.PERSONALIZACION
        ),
        Maquina(
            newId(),
            UUID.fromString("65b04ffb-052a-4813-bdf0-c15cd5833c48"),
            "Apple",
            "morado",
            LocalDate.now(),
            "true, 25.0, 9.1",
            TipoMaquina.PERSONALIZACION
        )
    )
}



suspend fun getPedidos(): List<Pedidos?> {
    return listOf(
        UsuarioRepositoryImpl().findByUUID(UUID.fromString("4747bf2d-22dd-4a29-89c8-c1a7492769ae"))?.let {
            Pedidos(
                0,
                UUID.randomUUID(),
                TipoEstado.EN_PROCESO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                null,
                120.5,
                it,
            )
        },
        UsuarioRepositoryImpl().findByUUID(UUID.fromString("4747bf2d-22dd-4a29-89c8-c1a7492769ae"))?.let {
            Pedidos(
                0,
                UUID.randomUUID(),
                TipoEstado.RECIBIDO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                LocalDate.of(2022, 12, 7),
                120.5,
                it,
            )
        },
        UsuarioRepositoryImpl().findByUUID(UUID.fromString("4747bf2d-22dd-4a29-89c8-c1a7492769ae"))?.let {
            Pedidos(
                0,
                UUID.randomUUID(),
                TipoEstado.TERMINADO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                null,
                120.5,
                it,
            )
        },
        UsuarioRepositoryImpl().findByUUID(UUID.fromString("4747bf2d-22dd-4a29-89c8-c1a7492769ae"))?.let {
            Pedidos(
                0,
                UUID.randomUUID(),
                TipoEstado.EN_PROCESO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                null,
                120.5,
                it.id,
            )
        }
    )
}

fun getProductos(): List<Producto>{
    return listOf(
        Producto(
            1,
            UUID.randomUUID(),
            "Wilson",
            "raqueta",
            20.2,
            12
        ),
        Producto(
            2,
            UUID.randomUUID(),
            "hola",
            "Overgrips",
            20.2,
            12
        ),
        Producto(
            3,
            UUID.randomUUID(),
            "adios",
            "grips",
            20.2,
            12
        )
    )
}

fun getTareas(): List<Tarea>{
    return listOf(
        Tarea(
          1,
            UUID.randomUUID(),
            ProductosRepositoryImpl().findById(1)!!,
            20.0,
            "Personalizacion",
            UsuarioRepositoryImpl().findById(3)!!,
            TurnosRepositoryImpl().findById(1)!!,
            true,
            MaquinaEncordarRepositoryImpl().findById(1)!!,
            null,
            PedidosRepositoryImpl().findById(1)!!
        ),
        Tarea(
            2,
            UUID.randomUUID(),
            ProductosRepositoryImpl().findById(3)!!,
            20.0,
            "Personalizacion",
            UsuarioRepositoryImpl().findById(2)!!,
            TurnosRepositoryImpl().findById(3)!!,
            true,
            MaquinaEncordarRepositoryImpl().findById(2)!!,
            null,
            PedidosRepositoryImpl().findById(3)!!
        ),
        Tarea(
            3,
            UUID.randomUUID(),
            ProductosRepositoryImpl().findById(2)!!,
            20.0,
            "Personalizacion",
            UsuarioRepositoryImpl().findById(2)!!,
            TurnosRepositoryImpl().findById(2)!!,
            true,
            MaquinaEncordarRepositoryImpl().findById(3)!!,
            null,
            PedidosRepositoryImpl().findById(3)!!
        ),
        Tarea(
            4,
            UUID.randomUUID(),
            ProductosRepositoryImpl().findById(1)!!,
            20.0,
            "Personalizacion",
            UsuarioRepositoryImpl().findById(3)!!,
            TurnosRepositoryImpl().findById(3)!!,
            true,
            null,
            MaquinaPersonalizacionRepositoryImpl().findById(1)!!,
            PedidosRepositoryImpl().findById(3)!!
        )
    )
}

