package com.samuelkiszka.virtuallibrary.data.mocks

import com.samuelkiszka.virtuallibrary.data.models.BookListModel

public class BookListModelMock {
    fun getBookList(): List<BookListModel> {
        return listOf<BookListModel>(
            BookListModel(
                id=1,
                title="Harry Potter and the Philosopher's Stone",
                author="J. K. Rowling",
            ),
            BookListModel(
                id=2,
                title="The Lord of the Rings",
                author="J. R. R. Tolkien",
            ),
            BookListModel(
                id=3,
                title="To Kill a Mockingbird",
                author="Harper Lee",
            )
        )
    }
}