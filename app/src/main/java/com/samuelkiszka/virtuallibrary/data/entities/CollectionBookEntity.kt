package com.samuelkiszka.virtuallibrary.data.entities

import androidx.room.Entity

@Entity(
    primaryKeys = ["collectionId", "bookId"],
    tableName = "collection_books"
)
data class CollectionBookEntity(
    val collectionId: Long,
    val bookId: Long
)
