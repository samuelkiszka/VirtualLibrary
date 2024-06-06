package com.samuelkiszka.virtuallibrary.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
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
        WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'
        ORDER BY title ASC
    """)
    fun getBookList(query: String): Flow<List<BookListModel>>

    @Query("""
        SELECT * 
        FROM books 
        WHERE id = :id
    """)
    fun getBookById(id: Long): Flow<BookEntity>

    @Query("""
        SELECT id, title
        FROM books
        WHERE id NOT IN (
            SELECT bookId
            FROM collection_books
            WHERE collectionId = :collectionId
        )
        ORDER BY title ASC
    """)
    fun getBooksNotInCollection(collectionId: Long): Flow<List<AddListItemModel>>

    @Query("""
        SELECT book.id, book.title, book.author, book.coverUrl
        FROM books book JOIN collection_books cb ON book.id = cb.bookId
        WHERE cb.collectionId = :collectionId
        ORDER BY book.title ASC
    """)
    fun getBooksInCollection(collectionId: Long): Flow<List<BookListModel>>
}