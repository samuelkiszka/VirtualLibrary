package com.samuelkiszka.virtuallibrary.network

import com.samuelkiszka.virtuallibrary.data.models.ListOfWorks
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("/search.json")
    suspend fun search_books(
        @Query("q") query: String = "",
        @Query("fields") fields: String = "isbn",
        @Query("language") language: String = "eng"
    ): ListOfWorks
}