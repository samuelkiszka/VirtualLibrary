package com.samuelkiszka.virtuallibrary.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailUiState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val yearPublished: String = "",
    val pagesRead: Int = 0,
    val numberOfPages: Int = 0,
    val notes: String = "",
    val coverUrl: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val rating: Float = 0f
){
    fun toLibraryDetailUiState(changed: Boolean = false): LibraryDetailUiState {
        return LibraryDetailUiState(
            book = this,
            changed = changed
        )
    }

    fun getJsonUrlEncoded(): String {
        val data = """
            {
                "id":$id,
                "title":"$title",
                "author":"$author",
                "yearPublished":"$yearPublished",
                "numberOfPages":$numberOfPages,
                "notes":"$notes",
                "coverUrl":"$coverUrl"
            }
        """
        return URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
    }
}