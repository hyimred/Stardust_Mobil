package com.ptrkcsak.stardust_mobil.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeRepository {
    private var homeRepository:HomeRepository?=null
    var postModelListLiveData : LiveData<List<Model>>?=null

    init {
        homeRepository = HomeRepository()
        postModelListLiveData = MutableLiveData()
    }

    fun fetchAllPosts(){
        postModelListLiveData = homeRepository?.fetchAllPosts()
    }
}