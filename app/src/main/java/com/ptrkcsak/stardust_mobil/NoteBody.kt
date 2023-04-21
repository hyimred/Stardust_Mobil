package com.ptrkcsak.stardust_mobil

import com.google.gson.annotations.SerializedName
import java.util.Date

data class NoteBody (
    @SerializedName("noteId") val noteId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("dateCreated") val dateCreated: Date,
    @SerializedName("dateUpdated") val dateUpdated: Date,
    @SerializedName("isArchived") val isArchived: Boolean,
    @SerializedName("dateArchived") val dateArchived: Date,
    @SerializedName("isBinned") val isBinned: Boolean,
    @SerializedName("dateBinned") val dateBinned: Date
)
