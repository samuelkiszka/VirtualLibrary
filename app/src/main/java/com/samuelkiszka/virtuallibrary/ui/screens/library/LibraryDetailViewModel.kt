package com.samuelkiszka.virtuallibrary.ui.screens.library

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.ui.screens.search.AddEditBookDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LibraryDetailUiState(
    var book: BookEntity = BookEntity(),
    var changed: Boolean = false
)

class LibraryDetailViewModel(
    val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle[LibraryDetailDestination.ARGS])

    var uiState by mutableStateOf(LibraryDetailUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = bookRepository.getBookByIdStream(id)
                .filterNotNull()
                .first()
                .toLibraryDetailUiState(changed = false)
        }
    }

    fun updateUiState(book: BookEntity) {
        uiState = LibraryDetailUiState(
            book = book,
            changed = true
        )
    }

    suspend fun saveChanges() {
        if (uiState.changed) {
            Log.d("API", uiState.book.toString())
            bookRepository.updateBook(
                uiState.book
            )
            uiState = uiState.copy(changed = false)
        }
    }

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
    }
}