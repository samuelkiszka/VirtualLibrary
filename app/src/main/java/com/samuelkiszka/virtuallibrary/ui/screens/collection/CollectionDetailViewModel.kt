package com.samuelkiszka.virtuallibrary.ui.screens.collection

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
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionEntity
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CollectionDetailUiState(
    var collection: CollectionEntity = CollectionEntity(),
    var books: List<BookListModel> = listOf()
)

class CollectionDetailViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var id: Long = checkNotNull(savedStateHandle[CollectionDetailDestination.ARGS])
    var uiState by mutableStateOf(CollectionDetailUiState())
        private set

    var dropdownMenuExpanded by mutableStateOf(false)
        private set

    var showDeleteAlert by mutableStateOf(false)
        private set

    fun toggleDropdownMenu() {
        dropdownMenuExpanded = !dropdownMenuExpanded
    }

    fun toggleDeleteAlert() {
        showDeleteAlert = !showDeleteAlert
    }

    fun deleteCollection() {
        viewModelScope.launch {
            appRepository.deleteCollection(uiState.collection)
        }
    }

    init {
        viewModelScope.launch {
            uiState = appRepository.getCollectionByIdStream(id)
                .filterNotNull()
                .first()
                .toCollectionDetailUiState()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.appRepository
                CollectionDetailViewModel(
                    appRepository = bookRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}