package com.ptrkcsak.stardust_mobil.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptrkcsak.stardust_mobil.home.data.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("data")
    fun fetchAllPosts(): Call<List<Model>>
}

fun fetchAllPosts(): LiveData<List<Model>> {
    val data = MutableLiveData<List<Model>>()

    apiInterface?.fetchAllPosts()?.enqueue(object : Callback<List<Model>> {

        override fun onFailure(call: Call<List<Model>>, t: Throwable) {
            data.value = null
        }

        override fun onResponse(
            call: Call<List<Model>>,
            response: Response<List<Model>>
        ) {

            val res = response.body()
            if (response.code() == 200 &&  res!=null){
                data.value = res
            }else{
                data.value = null
            }
        }
    })
    return data
}