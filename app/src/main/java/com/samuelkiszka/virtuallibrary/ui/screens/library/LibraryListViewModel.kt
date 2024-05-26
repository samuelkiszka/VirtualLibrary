package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.lifecycle.ViewModel
import com.samuelkiszka.virtuallibrary.data.mocks.BookListModelMock
import com.samuelkiszka.virtuallibrary.data.models.BookListModel

class LibraryListViewModel: ViewModel() {
    init {
        getBookList()
    }

    fun getBookList(): List<BookListModel> {
        return BookListModelMock().getBookList()
    }
}