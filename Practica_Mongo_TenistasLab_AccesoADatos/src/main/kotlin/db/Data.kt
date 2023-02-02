package db


import mapper.toUsuarioDto
import models.*
import models.enums.TipoEstado
import models.enums.TipoMaquina
import models.enums.TipoPerfil
import org.litote.kmongo.newId
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
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
            "5f6cab09-31a4-46d0-8d69-e4e510d2a227",
            LocalDateTime.of(2022, 12, 5, 8, 0),
            LocalDateTime.of(2022, 12, 5, 10, 0)
        ),
        Turno(
            newId(),
            "ee56f791-f545-466f-959f-4683d558c692",
            LocalDateTime.of(2022, 12, 5, 10, 0),
            LocalDateTime.of(2022, 12, 5, 12, 0)
        ),
        Turno(
            newId(),
            "b4023b8f-68a1-4d93-8519-8d0b54f7013d",
            LocalDateTime.of(2022, 12, 5, 12, 0),
            LocalDateTime.of(2022, 12, 5, 14, 0)
        ),
        Turno(
            newId(),
            "88a03bc5-9cde-40a9-9087-883b74339e75",
            LocalDateTime.of(2022, 12, 5, 14, 0),
            LocalDateTime.of(2022, 12, 5, 16, 0)
        )
    )
}

suspend fun getUsuarios(): List<Usuario?> {
    return listOf(
        TurnoRepositoryImpl().findByUUID("5f6cab09-31a4-46d0-8d69-e4e510d2a227")?.let {
            Usuario(
                newId(),
                "5e187396-a777-4a07-871d-90ed04e1af8a",
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
            "4747bf2d-22dd-4a29-89c8-c1a7492769ae",
            "Andrés",
            "Márquez",
            "andres.marquez@gmail.com",
            Password().encriptar("andresMarquez"),
            TipoPerfil.USUARIO,
            null,
            null
        ),
        TurnoRepositoryImpl().findByUUID("ee56f791-f545-466f-959f-4683d558c692")?.let {
            Usuario(
                newId(),
                "38ac290a-b3d5-4ef6-a9ab-8f24df5724f1",
                "Rubén",
                "García-Redondo",
                "rubengrm@gmail.com",
                Password().encriptar("rubengrm"),
                TipoPerfil.ENCORDADOR,
                it.id,
                null
            )
        },
        TurnoRepositoryImpl().findByUUID("b4023b8f-68a1-4d93-8519-8d0b54f7013d")?.let {
            Usuario(
                newId(),
                "2c172996-ce30-470a-b796-7b03e6224055",
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

fun getMaquinas(): List<Maquina> {
    return listOf(
        Maquina(
            newId(),
            "a016f77a-4698-4bd3-8294-1edb74311d27",
            "nadal",
            "rojo",
            LocalDate.now(),
            "true, 20.0, 12.4",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            "16479c98-4d85-44ed-b3a2-67b8c455024c",
            "nike",
            "verde",
            LocalDate.now(),
            "true,19.2, 10.4",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            "a1ec9a97-639f-4652-8dcb-4ef256ca96ab",
            "Adidas",
            "Azul",
            LocalDate.now(),
            "false, 22.0, 13.1,",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            "381ffc54-a2fe-47d9-ab45-04d9b21b293b",
            "Apple",
            "morado",
            LocalDate.now(),
            "true,25.0,9.1",
            TipoMaquina.ENCORDAR
        ),
        Maquina(
            newId(),
            "00339a54-6eb4-4a49-820c-dc49183a564a",
            "perso1",
            "rojo",
            LocalDate.now(),
            "true, 1.0, 10.4",
            TipoMaquina.PERSONALIZACION
        ),
        Maquina(
            newId(),
            "02c6bb52-ba65-41a9-8458-850d284bab07",
            "nike",
            "verde",
            LocalDate.now(),
            "true, 19.2, 10.4",
            TipoMaquina.PERSONALIZACION
        ),
        Maquina(
            newId(),
            "5e8a23a4-401f-4d3d-9f33-197419af57d9",
            "Adidas",
            "Azul",
            LocalDate.now(),
            "false, 22.0, 13.1",
            TipoMaquina.PERSONALIZACION
        ),
        Maquina(
            newId(),
            "65b04ffb-052a-4813-bdf0-c15cd5833c48",
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
        UsuarioRepositoryImpl().findByUUID("4747bf2d-22dd-4a29-89c8-c1a7492769ae")?.let {
            Pedidos(
                newId(),
                "45c3ca42-dc8f-46c7-9dfe-ff8fd786a77f",
                TipoEstado.EN_PROCESO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                null,
                120.5,
                it.id,
            )
        },
        UsuarioRepositoryImpl().findByUUID("4747bf2d-22dd-4a29-89c8-c1a7492769ae")?.let {
            Pedidos(
                newId(),
                "84f88209-dee6-461f-a516-b9f2ab7dd643",
                TipoEstado.RECIBIDO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                LocalDate.of(2022, 12, 7),
                120.5,
                it.id,
            )
        },
        UsuarioRepositoryImpl().findByUUID("4747bf2d-22dd-4a29-89c8-c1a7492769ae")?.let {
            Pedidos(
                newId(),
                "cc1278ea-86ce-41de-a84c-c3bff0b97731",
                TipoEstado.TERMINADO,
                LocalDate.now(),
                LocalDate.of(2022, 12, 6),
                null,
                120.5,
                it.id,
            )
        },
        UsuarioRepositoryImpl().findByUUID("4747bf2d-22dd-4a29-89c8-c1a7492769ae")?.let {
            Pedidos(
                newId(),
                "0b1dde0c-9138-418a-b528-62612ecc7782",
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

fun getProductos(): List<Producto> {
    return listOf(
        Producto(
            newId(),
            "cf1d57ca-410a-45a4-ae9a-dd1f40395aa5",
            "Wilson",
            "raqueta",
            20.2,
            12
        ),
        Producto(
            newId(),
            "74a17626-abb6-49a1-8f02-ab48d6a28e7c",
            "hola",
            "Overgrips",
            20.2,
            12
        ),
        Producto(
            newId(),
            "d560efac-9996-4272-9baa-e4a5979d3ede",
            "adios",
            "grips",
            20.2,
            12
        )
    )
}

suspend fun getTareas(): List<Tarea> {
    return listOf(
        Tarea(
            newId(),
            "d560efac-9996-4272-9baa-e4a5979d3ede",
            ProductoRepositoryImpl().findByUUID("cf1d57ca-410a-45a4-ae9a-dd1f40395aa5")!!,
            20.2,
            "Personalizacion",
            UsuarioRepositoryImpl().findByUUID("38ac290a-b3d5-4ef6-a9ab-8f24df5724f1")!!.toUsuarioDto(),
            TurnoRepositoryImpl().findByUUID("5f6cab09-31a4-46d0-8d69-e4e510d2a227")!!,
            true,
            MaquinaRepositoryImpl().findByUUID("00339a54-6eb4-4a49-820c-dc49183a564a")!!,
            PedidosRepositoryImpl().findByUUID("45c3ca42-dc8f-46c7-9dfe-ff8fd786a77f")!!
        ),
        Tarea(
            newId(),
            "d560efac-9996-4272-9baa-e4a5979d3ede",
            ProductoRepositoryImpl().findByUUID("d560efac-9996-4272-9baa-e4a5979d3ede")!!,
            20.0,
            "Personalizacion",
            UsuarioRepositoryImpl().findByUUID("5e187396-a777-4a07-871d-90ed04e1af8a")!!.toUsuarioDto(),
            TurnoRepositoryImpl().findByUUID("5f6cab09-31a4-46d0-8d69-e4e510d2a227")!!,
            true,
            MaquinaRepositoryImpl().findByUUID("02c6bb52-ba65-41a9-8458-850d284bab07")!!,
            PedidosRepositoryImpl().findByUUID("cc1278ea-86ce-41de-a84c-c3bff0b97731")!!
        ),
        Tarea(
            newId(),
            "d560efac-9996-4272-9baa-e4a5979d3ede",
            ProductoRepositoryImpl().findByUUID("74a17626-abb6-49a1-8f02-ab48d6a28e7c")!!,
            20.0,
            "Personalizacion",
            UsuarioRepositoryImpl().findByUUID("5e187396-a777-4a07-871d-90ed04e1af8a")!!.toUsuarioDto(),
            TurnoRepositoryImpl().findByUUID("5f6cab09-31a4-46d0-8d69-e4e510d2a227")!!,
            true,
            MaquinaRepositoryImpl().findByUUID("02c6bb52-ba65-41a9-8458-850d284bab07")!!,
            PedidosRepositoryImpl().findByUUID("cc1278ea-86ce-41de-a84c-c3bff0b97731")!!
        ),
        Tarea(
            newId(),
            "d560efac-9996-4272-9baa-e4a5979d3ede",
            ProductoRepositoryImpl().findByUUID("d560efac-9996-4272-9baa-e4a5979d3ede")!!,
            20.0,
            "Encordar",
            UsuarioRepositoryImpl().findByUUID("2c172996-ce30-470a-b796-7b03e6224055")!!.toUsuarioDto(),
            TurnoRepositoryImpl().findByUUID("b4023b8f-68a1-4d93-8519-8d0b54f7013d")!!,
            false,
            MaquinaRepositoryImpl().findByUUID("a016f77a-4698-4bd3-8294-1edb74311d27")!!,
            PedidosRepositoryImpl().findByUUID("0b1dde0c-9138-418a-b528-62612ecc7782")!!
        )
    )
}



