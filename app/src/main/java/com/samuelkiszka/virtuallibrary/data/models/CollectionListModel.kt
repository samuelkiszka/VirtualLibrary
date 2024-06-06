package com.samuelkiszka.virtuallibrary.data.models

data class CollectionListModel(
    val id: Long,
    val name: String,
    val books: List<BookCollectionListModel>
)
