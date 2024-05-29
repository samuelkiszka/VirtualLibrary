package com.samuelkiszka.virtuallibrary.data

import com.samuelkiszka.virtuallibrary.data.daos.BookDao
import com.samuelkiszka.virtuallibrary.data.daos.CollectionDao
import com.samuelkiszka.virtuallibrary.network.BookApiService

interface BookRepository {

}

class DefaultBookRepository(
    private val bookApiService: BookApiService,
    private val bookDao: BookDao,
    private val collectionDao: CollectionDao
) : BookRepository {

}