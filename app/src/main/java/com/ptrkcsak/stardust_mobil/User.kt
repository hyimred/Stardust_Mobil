package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("reregistartionDate") val regregistartionDate: String,
)
