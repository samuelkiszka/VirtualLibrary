package com.samuelkiszka.virtuallibrary.data.models

data class BookListModel(
    val id: Long,
    val title: String,
    val author: String,
    val coverUrl: String,
    val pagesRead: Int,
    val numberOfPages: Int,
    val rating: Float
)
