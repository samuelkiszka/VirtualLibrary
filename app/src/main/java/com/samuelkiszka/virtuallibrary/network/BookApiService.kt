package com.samuelkiszka.virtuallibrary.network

import com.samuelkiszka.virtuallibrary.data.models.BooksWrapper
import com.samuelkiszka.virtuallibrary.data.models.ListOfWorks
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("/search.json")
    suspend fun getWorksList(
        @Query("q") query: String = "harry potter",
        @Query("fields") fields: String = "key,editions,editions.key,editions.isbn",
        @Query("language") language: String = "eng",
        @Query("limit") limit: String = "2"
    ): ListOfWorks

    @GET("/api/books")
    suspend fun getBookByIsbn(
        @Query("bibkeys") isbn: String = "ISBN:9780545582995",
        @Query("format") format: String = "json",
        @Query("jscmd") jscmd: String = "data"
    ): BooksWrapper
}