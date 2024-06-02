package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samuelkiszka.virtuallibrary.VirtualLibraryApplication
import com.samuelkiszka.virtuallibrary.data.BookRepository
import com.samuelkiszka.virtuallibrary.data.mocks.BookListModelMock
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LibraryListUiState(
    val bookList: List<BookListModel> = emptyList()
)

class LibraryListViewModel(
    bookRepository: BookRepository
): ViewModel() {
    val uiState: StateFlow<LibraryListUiState> =
        bookRepository.getBookListStream()
            .map {
                LibraryListUiState(
                    bookList = it
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = LibraryListUiState()
            )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.bookRepository
                LibraryListViewModel(
                    bookRepository = bookRepository
                )
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L
    }
}