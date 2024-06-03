package com.samuelkiszka.virtuallibrary.data.models

import com.google.gson.Gson
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class AddEditBookModel(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val yearPublished: String = "",
    val numberOfPages: String = "0",
    val notes: String = "",
    val coverUrl: String = ""
) {
    fun fromJson(data: String): AddEditBookModel {
        val json = URLDecoder.decode(data, StandardCharsets.UTF_8.toString())
        return Gson().fromJson(json, AddEditBookModel::class.java)
    }

    fun toBookEntity(): BookEntity {
        return BookEntity(
            title = title,
            author = author,
            yearPublished = yearPublished,
            numberOfPages = numberOfPages.toInt(),
            notes = notes,
            coverUrl = coverUrl
        )
    }
}