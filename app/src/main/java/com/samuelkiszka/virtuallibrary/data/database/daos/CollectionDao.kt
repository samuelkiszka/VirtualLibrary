package com.samuelkiszka.virtuallibrary.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionEntity
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity): Long

    @Query("""
        UPDATE collections
        SET name = :name, description = :description
        WHERE id = :id
    """)
    suspend fun updateCollection(id: Long, name: String, description: String)

    @Query("""
        DELETE FROM collections
        WHERE id = :id
    """)
    suspend fun deleteCollection(id: Long)

    @Query("""
        SELECT * 
        FROM collections 
        WHERE id = :id
    """)
    fun getCollectionById(id: Long): Flow<CollectionEntity?>

    @Query("""
        SELECT * 
        FROM collections
    """)
    fun getCollectionList(): Flow<List<CollectionEntity>>


    @Query("""
        SELECT book.id, book.coverUrl 
        FROM books book LEFT JOIN collection_books cb ON book.id = cb.bookId
        WHERE cb.collectionId = :collectionId
    """)
    suspend fun getBooksByCollectionId(collectionId: Long): List<BookCollectionListModel>

    @Query("""
        REPLACE INTO collection_books (collectionId, bookId)
        VALUES (:collectionId, :bookId)
    """)
    suspend fun addBookToCollection(collectionId: Long, bookId: Long)

    @Query("""
        DELETE FROM collection_books
        WHERE collectionId = :collectionId AND bookId = :bookId
    """)
    suspend fun removeBookFromCollection(collectionId: Long, bookId: Long)

    @Query("""
        DELETE FROM collection_books
        WHERE collectionId = :collectionId
    """)
    suspend fun removeAllBooksFromCollection(collectionId: Long)

    @Query("""
        SELECT id, name as title
        FROM collections
        WHERE id NOT IN (
            SELECT collectionId
            FROM collection_books
            WHERE bookId = :bookId
        )
        ORDER BY title ASC
    """)
    fun getBookMissingCollections(bookId: Long): Flow<List<AddListItemModel>>

    @Query("""
        SELECT id, name as title
        FROM collections
        WHERE id IN (
            SELECT collectionId
            FROM collection_books
            WHERE bookId = :bookId
        )
    """)
    fun getBookCollections(bookId: Long): Flow<List<AddListItemModel>>
}