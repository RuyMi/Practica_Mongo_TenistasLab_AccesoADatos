package services.ktorfit

import de.jensklingenberg.ktorfit.http.*
import dto.*
import models.Usuario


interface KtorFitRest {

    @GET("users")
    suspend fun getAll(@Query("page") page: Int = 0, @Query("per_page") perPage: Int = 0): GetAllDto

    @GET("users/{id}")
    suspend fun getById(@Path("id") id: Int): GetByIdDto

    @POST("users")
    suspend fun create(@Body user: Usuario): CreateDto

    @PUT("users/{id}")
    suspend fun update(@Path("id") id: Long, @Body user: Usuario): UpdateDto

    @PATCH("users/{id}")
    suspend fun upgrade(@Path("id") id: Long, @Body user: Usuario): UpdateDto

    @DELETE("users/{id}")
    suspend fun delete(@Path("id") id: Long): Unit

}