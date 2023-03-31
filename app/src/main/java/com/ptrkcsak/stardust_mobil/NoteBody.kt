package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName

data class NoteBody (
    @SerializedName("noteId") val noteId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
)
