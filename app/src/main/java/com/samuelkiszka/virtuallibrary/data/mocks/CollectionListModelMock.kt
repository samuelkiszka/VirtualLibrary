package com.samuelkiszka.virtuallibrary.data.mocks

import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel

public class CollectionListModelMock {
    fun getCollectionList(): List<CollectionListModel> {
        return listOf<CollectionListModel>(
            CollectionListModel(
                id=1,
                name= "Harry Potter",
                books = listOf<BookCollectionListModel>(
                    BookCollectionListModel(
                        id=1,
                        coverUrl= ""
                    ),
                    BookCollectionListModel(
                        id=2,
                        coverUrl= ""
                    ),
                    BookCollectionListModel(
                        id=3,
                        coverUrl= ""
                    ),
                    BookCollectionListModel(
                        id=4,
                        coverUrl= ""
                    ),
                    BookCollectionListModel(
                        id=5,
                        coverUrl= ""
                    )
                ),
            )
        )
    }
}