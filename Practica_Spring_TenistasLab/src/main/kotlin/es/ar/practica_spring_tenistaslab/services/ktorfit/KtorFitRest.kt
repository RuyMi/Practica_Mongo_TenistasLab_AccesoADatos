package services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import dto.*
import es.ar.practica_spring_tenistaslab.models.Tarea
import es.ar.practica_spring_tenistaslab.models.Usuario
import es.ar.practica_spring_tenistaslab.models.UsuarioAPI




interface KtorFitRest {

    @GET("users")
    suspend fun getAll(): List<UsuarioAPI>

    @GET("users/{id}")
    suspend fun getById(@Path("id") id: Int): UsuarioAPI

    @POST("users")
    suspend fun create(@Body user: Usuario): CreateDto

    @PUT("users/{id}")
    suspend fun update(@Path("id") id: Long, @Body user: Usuario): UpdateDto

    @PATCH("users/{id}")
    suspend fun upgrade(@Path("id") id: Long, @Body user: Usuario): UpdateDto

    @DELETE("users/{id}")
    suspend fun delete(@Path("id") id: Long): Unit


    @POST("todos")
    suspend fun createTareas(@Body tarea: Tarea):Tarea

}