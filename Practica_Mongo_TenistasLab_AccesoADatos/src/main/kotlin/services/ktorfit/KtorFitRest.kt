package services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import dto.*
import models.Tarea
import models.Usuario
import models.UsuarioAPI


interface KtorFitRest {

    @GET("users")
    suspend fun getAll(): List<UsuarioAPI>

    @GET("users/{id}")
    suspend fun getById(@Path("id") id: Int): UsuarioAPI?

    @POST("users")
    suspend fun create(@Body user: Usuario): Usuario

    @PUT("users/{id}")
    suspend fun update(@Path("id") id: Long, @Body user: Usuario): UsuarioAPI

    @PATCH("users/{id}")
    suspend fun upgrade(@Path("id") id: Long, @Body user: Usuario): UsuarioAPI

    @DELETE("users/{id}")
    suspend fun delete(@Path("id") id: Long): Unit

    @POST("todos")
    suspend fun createTareas(@Body tarea: Tarea): Tarea

}