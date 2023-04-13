package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName
import java.util.*


data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("registartionDate") val registartionDate: Date,
)
