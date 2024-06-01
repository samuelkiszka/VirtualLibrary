package com.samuelkiszka.virtuallibrary.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.samuelkiszka.virtuallibrary.network.BookApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookRepository: BookRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {
    private val baseUrl = "https://openlibrary.org"
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }

    override val bookRepository: BookRepository by lazy {
        DefaultBookRepository(
            retrofitService,
            VirtualLibraryDatabase.getDatabase(context).bookDao(),
            VirtualLibraryDatabase.getDatabase(context).collectionDao()
        )
    }
}