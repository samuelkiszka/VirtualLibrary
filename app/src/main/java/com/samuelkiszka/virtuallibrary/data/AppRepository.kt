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
    // Book API
    suspend fun getIsbnList(query: String): List<String>
    suspend fun getBooksByQuery(query: String): List<BookApiModel>

    // Database - Book
    suspend fun saveBook(book: BookEntity): Long
    suspend fun updateBook(book: BookEntity): Long
    suspend fun updateBookBasicInfo(book: BookEntity): Long
    suspend fun deleteBook(book: BookEntity)
    fun getBookByIdStream(id: Long): Flow<BookEntity?>
    fun getBookListStream(query: String = ""): Flow<List<BookListModel>>
    fun getBooksNotInCollection(collectionId: Long): Flow<List<AddListItemModel>>
    fun getBooksInCollection(collectionId: Long): Flow<List<BookListModel>>
    fun getBookCollections(bookId: Long): Flow<List<AddListItemModel>>
    fun getBookMissingCollections(bookId: Long): Flow<List<AddListItemModel>>

    // Database - Collection
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
    /*
        *
        * Book API
        *
     */
    /*
        * This function is used to get a list of ISBNs from the Open Library API
     */
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
    /*
        * This function is used to get a list of books from the Open Library API
     */
    override suspend fun getBooksByQuery(query: String): List<BookApiModel> {
        val isbnList = this.getIsbnList(query)
        val bookList = mutableListOf<BookApiModel>()
        for (isbn in isbnList){
            val response = bookApiService.getBookByIsbn(isbn="isbn:$isbn")
            bookList.add(response.books.values.first())
        }
        return bookList
    }

    /*
        *
        * Database - Book
        *
     */
    /*
        * This function is used to save a book to the database
     */
    override suspend fun saveBook(book: BookEntity): Long {
        return bookDao.insertBook(book)
    }
    /*
        * This function is used to update a book in the database
     */
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
    /*
        * This function is used to update the basic information of a book in the database
     */
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
    /*
        * This function is used to delete a book from the database
     */
    override suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book.id)
    }
    /*
        * This function is used to get a book by its ID from the database
     */
    override fun getBookByIdStream(id: Long): Flow<BookEntity?> {
        return bookDao.getBookById(id)
    }
    /*
        * This function is used to get a list of books from the database
     */
    override fun getBookListStream(query: String): Flow<List<BookListModel>> {
        return bookDao.getBookList(query)
    }
    /*
        * This function is used to get a list of books that are not in a collection from the database
     */
    override fun getBooksNotInCollection(collectionId: Long): Flow<List<AddListItemModel>> {
        return bookDao.getBooksNotInCollection(collectionId)
    }
    /*
        * This function is used to get a list of books that are in a collection from the database
     */
    override fun getBooksInCollection(collectionId: Long): Flow<List<BookListModel>> {
        return bookDao.getBooksInCollection(collectionId)
    }
    /*
        * This function is used to get a list of collections that a book is in from the database
     */
    override fun getBookCollections(bookId: Long): Flow<List<AddListItemModel>> {
        return collectionDao.getBookCollections(bookId)
    }
    /*
        * This function is used to get a list of collections that a book is not in from the database
     */
    override fun getBookMissingCollections(bookId: Long): Flow<List<AddListItemModel>> {
        return collectionDao.getBookMissingCollections(bookId)
    }

    /*
        *
        * Database - Collection
        *
     */
    /*
        * This function is used to add a collection to the database
     */
    override suspend fun addCollection(collection: CollectionEntity): Long {
        return collectionDao.insertCollection(collection)
    }
    /*
        * This function is used to update a collection in the database
     */
    override suspend fun updateCollection(collection: CollectionEntity): Long {
        collectionDao.updateCollection(
            id = collection.id,
            name = collection.name,
            description = collection.description
        )
        return collection.id
    }
    /*
        * This function is used to delete a collection from the database
     */
    override suspend fun deleteCollection(collection: CollectionEntity) {
        collectionDao.removeAllBooksFromCollection(collection.id)
        collectionDao.deleteCollection(collection.id)
    }
    /*
        * This function is used to get a collection by its ID from the database
     */
    override fun getCollectionByIdStream(id: Long): Flow<CollectionEntity?> {
        return collectionDao.getCollectionById(id)
    }
    /*
        * This function is used to get a list of collections from the database
     */
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
    /*
        * This function is used to get a list of books in a collection from the database
     */
    override suspend fun getCollectionBooks(collectionId: Long): List<BookCollectionListModel> {
        return collectionDao.getBooksByCollectionId(collectionId)
    }
    /*
        * This function is used to add a book to a collection in the database
     */
    override suspend fun addBookToCollection(collectionId: Long, bookId: Long) {
        collectionDao.addBookToCollection(collectionId, bookId)
    }
    /*
        * This function is used to remove a book from a collection in the database
     */
    override suspend fun removeBookFromCollection(collectionId: Long, bookId: Long) {
        collectionDao.removeBookFromCollection(collectionId, bookId)
    }
}