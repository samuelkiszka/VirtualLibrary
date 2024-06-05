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
        SET rating = :rating, pagesRead = :pagesRead, notes = :notes, startDate = :startDate, endDate = :endDate
        WHERE id = :id
    """)
    suspend fun updateBook(id: Long, rating: Float, pagesRead: Int, notes: String, startDate: String, endDate: String)

    @Query("""
        UPDATE books
        SET title = :title, author = :author, yearPublished = :yearPublished, numberOfPages = :numberOfPages, notes = :notes
        WHERE id = :id
    """)
    suspend fun updateBookBasicInfo(id: Long, title: String, author: String, yearPublished: String, numberOfPages: Int, notes: String)

    @Query("""
        DELETE FROM books
        WHERE id = :id
    """)
    suspend fun deleteBook(id: Long)

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