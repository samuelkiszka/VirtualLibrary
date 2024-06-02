package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samuelkiszka.virtuallibrary.VirtualLibraryApplication
import com.samuelkiszka.virtuallibrary.data.BookRepository
import com.samuelkiszka.virtuallibrary.data.entities.BookEntity
import com.samuelkiszka.virtuallibrary.ui.screens.search.AddEditBookDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LibraryDetailUiState(
    var book: BookEntity = BookEntity()
)

class LibraryDetailViewModel(
    val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle[LibraryDetailDestination.ARGS])

    val uiState: StateFlow<LibraryDetailUiState> =
        bookRepository.getBookByIdStream(id)
            .filterNotNull()
            .map {
                LibraryDetailUiState(
                    book = it
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = LibraryDetailUiState()
            )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.bookRepository
                LibraryDetailViewModel(
                    bookRepository = bookRepository,
                    this.createSavedStateHandle()
                )
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L
    }
}