package com.samuelkiszka.virtuallibrary.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity): Long

    @Query("""
        UPDATE books
        SET rating = :rating, pagesRead = :pagesRead, notes = :notes
        WHERE id = :id
    """)
    suspend fun updateBook(id: Long, rating: Float, pagesRead: Int, notes: String)

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