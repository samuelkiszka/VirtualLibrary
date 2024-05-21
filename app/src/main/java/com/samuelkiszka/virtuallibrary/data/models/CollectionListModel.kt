package com.samuelkiszka.virtuallibrary.data.models

data class CollectionListModel(
    val id: Int,
    val name: String,
    val books: List<BookCollectionListModel>
)
