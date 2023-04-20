package com.ptrkcsak.stardust_mobil

import java.util.Date

data class ItemsViewModel(
    val title: String,
    val desc: String,
    val noteId: String,
    val dateCreated: Date,
    val dateUpdated: Date,
    val dateArchived: Date? = null,
    val dateDeleted: Date? = null,
    val isArchived: Boolean,
    val isDeleted: Boolean
) {
}