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
                        image= R.drawable.demo_book_cover
                    ),
                    BookCollectionListModel(
                        id=2,
                        image= R.drawable.demo_book_cover
                    ),
                    BookCollectionListModel(
                        id=3,
                        image= R.drawable.demo_book_cover
                    ),
                    BookCollectionListModel(
                        id=4,
                        image= R.drawable.demo_book_cover
                    ),
                    BookCollectionListModel(
                        id=5,
                        image= R.drawable.demo_book_cover
                    )
                ),
            ),
            CollectionListModel(
                id=2,
                name= "Books I have read",
                books = listOf<BookCollectionListModel>(
                    BookCollectionListModel(
                        id=1,
                        image= R.drawable.demo_book_cover
                    ),
                    BookCollectionListModel(
                        id=2,
                        image= R.drawable.demo_book_cover
                    )
                )
            )
        )
    }
}