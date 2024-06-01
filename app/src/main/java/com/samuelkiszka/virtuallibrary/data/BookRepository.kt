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
        var response = bookApiService.getWorksList(query=query)
        Log.d("API", response.toString())
        var isbnList = mutableListOf<String>()
        for (work in response.docs){
            isbnList.add(work.editions.docs[0].isbn[0])
        }
        return isbnList
    }

    override suspend fun getBooksByQuery(query: String): List<BookApiModel> {
        var isbnList = this.getIsbnList(query)
        var bookList = mutableListOf<BookApiModel>()
        for (isbn in isbnList){
            var response = bookApiService.getBookByIsbn(isbn="isbn:$isbn")
            bookList.add(response.books.values.first())
            Log.d("API", response.books.values.first().title)
        }
        return bookList
    }
}