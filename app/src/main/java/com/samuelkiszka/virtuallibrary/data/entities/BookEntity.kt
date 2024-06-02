package com.samuelkiszka.virtuallibrary.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.jetbrains.annotations.NotNull

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val author: String = "",
    val yearPublished: String = "",
    val numberOfPages: Int = 0,
    val notes: String = "",
    val coverUrl: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.NOT_STARTED
)

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