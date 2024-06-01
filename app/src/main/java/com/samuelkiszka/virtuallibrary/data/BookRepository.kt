package com.samuelkiszka.virtuallibrary.data

import android.util.Log
import com.samuelkiszka.virtuallibrary.data.daos.BookDao
import com.samuelkiszka.virtuallibrary.data.daos.CollectionDao
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import com.samuelkiszka.virtuallibrary.network.BookApiService

interface BookRepository {
    suspend fun getIsbnList(query: String): List<String>
    suspend fun getBooksByQuery(query: String): List<BookApiModel>
}

class DefaultBookRepository(
    private val bookApiService: BookApiService,
    private val bookDao: BookDao,
    private val collectionDao: CollectionDao
) : BookRepository {
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
}