package com.samuelkiszka.virtuallibrary.ui.screens.library

import DateUtils
import android.util.Log
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

data class LibraryDetailUiState(
    var book: BookEntity = BookEntity(),
    var changed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
class LibraryDetailViewModel(
    val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle[LibraryDetailDestination.ARGS])

    var uiState by mutableStateOf(LibraryDetailUiState())
        private set

    @OptIn(ExperimentalMaterial3Api::class)
    var startDate by mutableStateOf(DatePickerState(locale = Locale("English")))
        private set

    var showStartDatePicker by mutableStateOf(false)
        private set

    fun changeStartDatePickerVisibility() {
        showStartDatePicker = ! showStartDatePicker
    }

    @OptIn(ExperimentalMaterial3Api::class)
    var endDate by mutableStateOf(DatePickerState(locale = Locale("English")))
        private set

    var showEndDatePicker by mutableStateOf(false)
        private set

    fun changeEndDatePickerVisibility() {
        showEndDatePicker = ! showEndDatePicker
    }

    var duration by mutableStateOf(0)
        private set

    init {
        viewModelScope.launch {
            uiState = bookRepository.getBookByIdStream(id)
                .filterNotNull()
                .first()
                .toLibraryDetailUiState(changed = false)
            startDate.selectedDateMillis = DateUtils().stringToMillis(uiState.book.startDate)
            endDate.selectedDateMillis = DateUtils().stringToMillis(uiState.book.endDate)
            updateDuration()
        }
    }

    fun updateRating(newRating: Float) {
        updateUiState(
            uiState.book.copy(
                rating = newRating
            )
        )
    }

    fun updatePagesRead(newPagesRead: Int) {
        if (newPagesRead >=0 && newPagesRead <= uiState.book.numberOfPages) {
            updateUiState(
                uiState.book.copy(
                    pagesRead = newPagesRead
                )
            )
            if (uiState.book.startDate.isEmpty()) {
                updateStartDate(System.currentTimeMillis())
            }
            else if (uiState.book.endDate.isEmpty() && newPagesRead == uiState.book.numberOfPages) {
                updateEndDate(System.currentTimeMillis())
            }
        }

    }

    fun updateNotes(newNotes: String) {
        updateUiState(
            uiState.book.copy(
                notes = newNotes
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateStartDate(millis: Long) {
        if (millis != 0L) {
            val date = DateUtils().millisToString(millis)
            updateUiState(
                uiState.book.copy(
                    startDate = date
                )
            )
        }
        if (millis > (endDate.selectedDateMillis ?: millis)) {
            updateEndDate(millis)
        }
        updateDuration()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateEndDate(millis: Long) {
        if (millis != 0L) {
            val date = DateUtils().millisToString(millis)
            updateUiState(
                uiState.book.copy(
                    endDate = date
                )
            )
        }
        if (uiState.book.startDate.isEmpty() || millis < (startDate.selectedDateMillis ?: 0L)) {
            updateStartDate(millis)
        }
        updateDuration()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun updateDuration() {
        val start = startDate.selectedDateMillis ?: 0L
        val end = endDate.selectedDateMillis ?: 0L
        this.duration =
            if (start == 0L) {
                -1
            } else if (end == 0L) {
                DateUtils().millisToDays(System.currentTimeMillis() - start)
            }
            else {
                DateUtils().millisToDays(end - start)
            }
    }

    private fun updateUiState(book: BookEntity) {
        uiState = LibraryDetailUiState(
            book = book,
            changed = true
        )
    }

    suspend fun saveChanges() {
        if (uiState.changed) {
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