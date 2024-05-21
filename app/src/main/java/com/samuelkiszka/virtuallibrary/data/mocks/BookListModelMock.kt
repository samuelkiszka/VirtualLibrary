package com.samuelkiszka.virtuallibrary.data.mocks

import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookListModel

public class BookListModelMock {
    fun getBookList(): List<BookListModel> {
        return listOf<BookListModel>(
            BookListModel(
                id=1,
                title="Harry Potter and the Philosopher's Stone",
                author="J. K. Rowling",
                image= R.drawable.demo_book_cover
            ),
            BookListModel(
                id=2,
                title="The Lord of the Rings",
                author="J. R. R. Tolkien",
                image= R.drawable.demo_book_cover
            ),
            BookListModel(
                id=3,
                title="To Kill a Mockingbird",
                author="Harper Lee",
                image= R.drawable.demo_book_cover
            ),
            BookListModel(
                id=4,
                title="Game of Thrones",
                author="J. R. R. Martin",
                image= R.drawable.demo_book_cover
            ),
            BookListModel(
                id=5,
                title="The Lord of the Rings",
                author="J. R. R. Tolkien",
                image= R.drawable.demo_book_cover
            ),
            BookListModel(
                id=5,
                title="Seventh sense",
                author="Noami Novik",
                image= R.drawable.demo_book_cover
            ),
            BookListModel(
                id=6,
                title="Seventh sense",
                author="Noami Novik",
                image= R.drawable.demo_book_cover
            )
        )
    }
}