package com.samuelkiszka.virtuallibrary.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailUiState
import org.jetbrains.annotations.NotNull
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
    val numberOfPages: Int = 0,
    val notes: String = "",
    val coverUrl: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.NOT_STARTED,
    val pagesRead: Int = 0,
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

enum class ReadingStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}

class ReadingStatusConverter {
    @TypeConverter
    fun readingStatusFromString(value: Int): ReadingStatus {
        return when (value) {
            ReadingStatus.NOT_STARTED.ordinal -> ReadingStatus.NOT_STARTED
            ReadingStatus.IN_PROGRESS.ordinal -> ReadingStatus.IN_PROGRESS
            ReadingStatus.FINISHED.ordinal -> ReadingStatus.FINISHED
            else -> throw IllegalArgumentException("Invalid reading status")
        }
    }

    @TypeConverter
    fun readingStatusToInt(value: ReadingStatus): Int {
        return value.ordinal
    }
}