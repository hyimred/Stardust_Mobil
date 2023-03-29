package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName
import com.ptrkcsak.stardust_mobil.Constans.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("/auth/login")
    fun signin(@Body info: SignInBody): retrofit2.Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("/auth/signup")
    fun registerUser(@Body info: UserBody): retrofit2.Call<ResponseBody>
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