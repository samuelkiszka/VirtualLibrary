package com.samuelkiszka.virtuallibrary.ui.screens.library

import com.samuelkiszka.virtuallibrary.utils.DateUtils
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
import com.samuelkiszka.virtuallibrary.data.AppRepository
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class LibraryDetailUiState(
    var book: BookEntity = BookEntity(),
    var changed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
class LibraryDetailViewModel(
    val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle[LibraryDetailDestination.ARGS])

    var uiState by mutableStateOf(LibraryDetailUiState())
        private set

    var bookCollections: StateFlow<List<AddListItemModel>> =
        appRepository.getBookCollections(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listOf()
            )

    var otherCollections: StateFlow<List<AddListItemModel>> =
        appRepository.getBookMissingCollections(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listOf()
            )

    var dropdownMenuExpanded by mutableStateOf(false)
        private set

    var showDeleteAlert by mutableStateOf(false)
        private set

    var showManageCollectionsDialog by mutableStateOf(false)
        private set

    var addCollectionOption by mutableStateOf(true)
        private set

    fun showCollectionsToAdd(addCollectionOption: Boolean) {
        this.addCollectionOption = addCollectionOption
    }

    fun toggleManageCollectionsDialog() {
        showManageCollectionsDialog = !showManageCollectionsDialog
    }

    fun addBookToCollection(collectionId: Long) {
        viewModelScope.launch {
            appRepository.addBookToCollection(collectionId, id)
        }
    }

    fun removeBookFromCollection(collectionId: Long) {
        viewModelScope.launch {
            appRepository.removeBookFromCollection(collectionId, id)
        }
    }

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
            uiState = appRepository.getBookByIdStream(id)
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

    fun toggleDropdownMenu() {
        dropdownMenuExpanded = !dropdownMenuExpanded
    }

    fun toggleDeleteAlert() {
        showDeleteAlert = !showDeleteAlert
    }

    private fun updateUiState(book: BookEntity) {
        uiState = LibraryDetailUiState(
            book = book,
            changed = true
        )
    }

    suspend fun saveChanges() {
        if (uiState.changed) {
            appRepository.updateBook(
                uiState.book
            )
            uiState = uiState.copy(changed = false)
        }
    }

     fun deleteBook() {
        viewModelScope.launch {
            appRepository.deleteBook(uiState.book)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.appRepository
                LibraryDetailViewModel(
                    appRepository = bookRepository,
                    this.createSavedStateHandle()
                )
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L
    }
}