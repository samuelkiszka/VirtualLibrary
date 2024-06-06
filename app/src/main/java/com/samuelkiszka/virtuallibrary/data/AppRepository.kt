package com.samuelkiszka.virtuallibrary.data

import android.util.Log
import com.samuelkiszka.virtuallibrary.data.database.daos.BookDao
import com.samuelkiszka.virtuallibrary.data.database.daos.CollectionDao
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionEntity
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel
import com.samuelkiszka.virtuallibrary.data.network.BookApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppRepository {
    suspend fun getIsbnList(query: String): List<String>
    suspend fun getBooksByQuery(query: String): List<BookApiModel>
    suspend fun saveBook(book: BookEntity): Long
    suspend fun updateBook(book: BookEntity): Long
    suspend fun updateBookBasicInfo(book: BookEntity): Long
    suspend fun deleteBook(book: BookEntity)
    fun getBookByIdStream(id: Long): Flow<BookEntity?>
    fun getBookListStream(): Flow<List<BookListModel>>
    fun getBooksNotInCollection(collectionId: Long): Flow<List<AddListItemModel>>
    fun getBooksInCollection(collectionId: Long): Flow<List<BookListModel>>

    suspend fun addCollection(collection: CollectionEntity): Long
    suspend fun updateCollection(collection: CollectionEntity): Long
    suspend fun deleteCollection(collection: CollectionEntity)
    fun getCollectionByIdStream(id: Long): Flow<CollectionEntity?>
    fun getCollectionListStream(): Flow<List<CollectionListModel>>
    suspend fun getCollectionBooks(collectionId: Long): List<BookCollectionListModel>
    suspend fun addBookToCollection(collectionId: Long, bookId: Long)
    suspend fun removeBookFromCollection(collectionId: Long, bookId: Long)
}

class DefaultAppRepository(
    private val bookApiService: BookApiService,
    private val bookDao: BookDao,
    private val collectionDao: CollectionDao
) : AppRepository {
    override suspend fun getIsbnList(query: String): List<String> {
        val response = bookApiService.getWorksList(query=query)
        val isbnList = mutableListOf<String>()
        for (work in response.docs){
            val isbn = work.editions.docs[0].isbn
            if (isbn.isNotEmpty()){
                isbnList.add(isbn[0])
            }
        }
        return isbnList
    }

    override suspend fun getBooksByQuery(query: String): List<BookApiModel> {
        val isbnList = this.getIsbnList(query)
        val bookList = mutableListOf<BookApiModel>()
        for (isbn in isbnList){
            val response = bookApiService.getBookByIsbn(isbn="isbn:$isbn")
            bookList.add(response.books.values.first())
        }
        return bookList
    }

    override suspend fun saveBook(book: BookEntity): Long {
        return bookDao.insertBook(book)
    }

    override suspend fun updateBook(book: BookEntity): Long {
        bookDao.updateBook(
            id = book.id,
            rating = book.rating,
            pagesRead = book.pagesRead,
            notes = book.notes,
            startDate = book.startDate,
            endDate = book.endDate
        )
        return book.id
    }

    override suspend fun updateBookBasicInfo(book: BookEntity): Long {
        bookDao.updateBookBasicInfo(
            id = book.id,
            title = book.title,
            author = book.author,
            yearPublished = book.yearPublished,
            numberOfPages = book.numberOfPages,
            notes = book.notes
        )
        return book.id
    }

    override suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book.id)
    }

    override fun getBookByIdStream(id: Long): Flow<BookEntity?> {
        return bookDao.getBookById(id)
    }

    override fun getBookListStream(): Flow<List<BookListModel>> {
        return bookDao.getBookList()
    }

    override fun getBooksNotInCollection(collectionId: Long): Flow<List<AddListItemModel>> {
        return bookDao.getBooksNotInCollection(collectionId)
    }

    override fun getBooksInCollection(collectionId: Long): Flow<List<BookListModel>> {
        return bookDao.getBooksInCollection(collectionId)
    }

    override suspend fun addCollection(collection: CollectionEntity): Long {
        return collectionDao.insertCollection(collection)
    }

    override suspend fun updateCollection(collection: CollectionEntity): Long {
        collectionDao.updateCollection(
            id = collection.id,
            name = collection.name,
            description = collection.description
        )
        return collection.id
    }

    override suspend fun deleteCollection(collection: CollectionEntity) {
        collectionDao.deleteCollection(collection.id)
    }

    override fun getCollectionByIdStream(id: Long): Flow<CollectionEntity?> {
        return collectionDao.getCollectionById(id)
    }

    override fun getCollectionListStream(): Flow<List<CollectionListModel>> {
        return collectionDao.getCollectionList().map { collectionList ->
            collectionList.map { collection ->
                CollectionListModel(
                    id = collection.id,
                    name = collection.name,
                    books = getCollectionBooks(collection.id)
                )
            }
        }
    }

    override suspend fun getCollectionBooks(collectionId: Long): List<BookCollectionListModel> {
        return collectionDao.getBooksByCollectionId(collectionId)
    }

    override suspend fun addBookToCollection(collectionId: Long, bookId: Long) {
        collectionDao.addBookToCollection(collectionId, bookId)
    }

    override suspend fun removeBookFromCollection(collectionId: Long, bookId: Long) {
        collectionDao.removeBookFromCollection(collectionId, bookId)
    }

}