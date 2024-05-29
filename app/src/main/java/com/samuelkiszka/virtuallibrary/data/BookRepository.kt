package com.samuelkiszka.virtuallibrary.data

import android.util.Log
import com.samuelkiszka.virtuallibrary.data.daos.BookDao
import com.samuelkiszka.virtuallibrary.data.daos.CollectionDao
import com.samuelkiszka.virtuallibrary.network.BookApiService

interface BookRepository {
    suspend fun getIsbnList(query: String): List<String>
}

class DefaultBookRepository(
    private val bookApiService: BookApiService,
    private val bookDao: BookDao,
    private val collectionDao: CollectionDao
) : BookRepository {
    override suspend fun getIsbnList(query: String): List<String> {
        var response = bookApiService.search_books(query)
        var isbnList = mutableListOf<String>()
        for (isbn in response.docs){
            isbnList.add(isbn.isbn[0])
        }
        return isbnList
    }
}