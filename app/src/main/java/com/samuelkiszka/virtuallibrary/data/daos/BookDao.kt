package com.samuelkiszka.virtuallibrary.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samuelkiszka.virtuallibrary.data.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity): Long

    @Query("""
        SELECT id, title, author, coverUrl
        FROM books
        ORDER BY title ASC
    """)
    fun getBookList(): Flow<List<BookListModel>>

    @Query("""
        SELECT * 
        FROM books 
        WHERE id = :id
    """)
    fun getBookById(id: Long): Flow<BookEntity>
}