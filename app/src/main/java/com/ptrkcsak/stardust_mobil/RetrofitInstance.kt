package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName
import com.ptrkcsak.stardust_mobil.Constans.BASE_URL
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.net.ConnectException

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("/auth/login")
    fun signin(@Body info: SignInBody): retrofit2.Call<ResponseBody>
    @Headers("Content-Type:application/json")
    @POST("/auth/signup")
    fun registerUser(@Body info: UserBody): retrofit2.Call<ResponseBody>
    @GET("/notes")
    suspend fun getAllNotes(): Response<List<NoteBody>>
    @GET("/notes/{Id}")
    suspend fun getNote(@Path("Id") noteId: String): Response<NoteBody>
    @POST("/notes/god")
    suspend fun postGod(): Response<Unit>
    @POST("/notes")
    suspend fun postNote(@Body requestBody: RequestBody): Response<ResponseBody>
    @GET("/account/profile")
    suspend fun getProfile(): Response<User>
    @PATCH("/account")
    suspend fun updateProfile(@Body requestBody: RequestBody): Response<Unit>
    @DELETE("/account")
    suspend fun deleteProfile(): Response<Unit>
    @DELETE("/notes/{Id}")
    suspend fun deleteNote(@Path("Id") noteId: String): Response<ResponseBody>
    @PATCH("/notes/bin/{Id}")
    suspend fun binNote(@Path("Id") noteId: String): Response<ResponseBody>
    @PATCH("/notes/unbin/{Id}")
    suspend fun unbinNote(@Path("Id") noteId: String): Response<ResponseBody>
    @PATCH("/notes/archive/{Id}")
    suspend fun archiveNote(@Path("Id") noteId: String): Response<ResponseBody>
    @PATCH("/notes/unarchive/{Id}")
    suspend fun unarchiveNote(@Path("Id") noteId: String): Response<ResponseBody>
    @PATCH("/notes/{Id}")
    suspend fun editNote(@Path("Id") noteId: String, @Body requestBody: RequestBody): Response<ResponseBody>
}
class RetrofitInstance {
    companion object {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}