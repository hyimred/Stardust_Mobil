package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName
import com.ptrkcsak.stardust_mobil.Constans.BASE_URL
import com.ptrkcsak.stardust_mobil.Constans.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("/auth/login")
    fun signin(@Body info: SignInBody): retrofit2.Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("/auth/signup")
    fun registerUser(@Body info: UserBody): retrofit2.Call<ResponseBody>
    @GET("/notes")
    fun getAllNotes(): Call<ArrayList<NoteBody>?>?
    @GET("/profile")
    fun getProfile(): User

    data class LoginResponse(
        @SerializedName("access_token")
        val token: String
    )
}
class RetrofitInstance {
    companion object {

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        @Throws(IOException::class)
        fun intercept(chain: Interceptor.Chain): okhttp3.Response {

            //rewrite the request to add bearer token
            val newRequest: Request = chain.request().newBuilder()
                .header("Authorization", "Bearer $USER_TOKEN")
                .build()
            return chain.proceed(newRequest)
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