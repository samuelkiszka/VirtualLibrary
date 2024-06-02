package com.samuelkiszka.virtuallibrary.data.models

import android.util.Log
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class AddEditBookModel(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val yearPublished: String = "",
    val numberOfPages: Int = 0,
    val notes: String = "",
    val coverUrl: String = ""
) {
    fun fromJson(data: String): AddEditBookModel {
        val json = URLDecoder.decode(data, StandardCharsets.UTF_8.toString())
        return Gson().fromJson(json, AddEditBookModel::class.java)
    }
}