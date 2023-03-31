package com.ptrkcsak.stardust_mobil

import com.ptrkcsak.stardust_mobil.Constans.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class TokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        //rewrite the request to add bearer token
        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer $USER_TOKEN")
            .build()
        return chain.proceed(newRequest)
    }
}