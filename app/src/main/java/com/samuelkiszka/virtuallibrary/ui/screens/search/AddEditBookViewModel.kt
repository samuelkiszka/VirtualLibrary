package com.samuelkiszka.virtuallibrary.ui.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samuelkiszka.virtuallibrary.VirtualLibraryApplication
import com.samuelkiszka.virtuallibrary.data.BookRepository
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.models.AddEditBookModel

data class AddEditUiState(
    var book: AddEditBookModel = AddEditBookModel(),
)

class AddEditBookViewModel(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var addEditUiState: AddEditUiState by mutableStateOf(AddEditUiState())
        private set

    var args: String? = savedStateHandle[AddEditBookDestination.ARGS]

    var id by mutableIntStateOf(0)
        private set

    var title by mutableStateOf("")
        private set

    var author by mutableStateOf("")
        private set

    var yearPublished by mutableStateOf("")
        private set

    var numberOfPages by mutableStateOf("")
        private set

    var coverUrl by mutableStateOf("")
        private set

    fun updateId(newId: Int) {
        id = newId
    }

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateAuthor(newAuthor: String) {
        author = newAuthor
    }

    fun updateYearPublished(newYearPublished: String) {
        yearPublished = newYearPublished
    }

    fun updateNumberOfPages(newNumberOfPages: String) {
        if (newNumberOfPages.isEmpty() || newNumberOfPages.matches(Regex("^\\d+\$"))) {
            numberOfPages = newNumberOfPages
        }
    }

    fun updateCoverUrl(newCoverUrl: String) {
        coverUrl = newCoverUrl
    }

    fun populateData() {
        if (!args.isNullOrEmpty()){
            addEditUiState.book = AddEditBookModel().fromJson(args!!)
        }
        updateId(addEditUiState.book.id)
        updateTitle(addEditUiState.book.title)
        updateAuthor(addEditUiState.book.author)
        updateYearPublished(addEditUiState.book.yearPublished)
        updateNumberOfPages(addEditUiState.book.numberOfPages)
        updateCoverUrl(addEditUiState.book.coverUrl)
    }

    suspend fun saveBook(): Long {
        val entity = AddEditBookModel(
            id = id,
            title = title,
            author = author,
            yearPublished = yearPublished,
            numberOfPages = numberOfPages,
            coverUrl = coverUrl
        ).toBookEntity()
        if (entity.id != 0L) {
            bookRepository.updateBookBasicInfo(entity)
            return entity.id
        } else {
            return bookRepository.saveBook(entity)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.bookRepository
                AddEditBookViewModel(
                    bookRepository = bookRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}
