package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samuelkiszka.virtuallibrary.VirtualLibraryApplication
import com.samuelkiszka.virtuallibrary.data.AppRepository
import com.samuelkiszka.virtuallibrary.data.mocks.CollectionListModelMock
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListUiState
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CollectionListUiState(
    val collectionList: List<CollectionListModel> = emptyList()
)

class CollectionListViewModel(
    val appRepository: AppRepository,
): ViewModel() {

    var uiState: StateFlow<CollectionListUiState> =
        appRepository.getCollectionListStream()
            .map {
                CollectionListUiState(
                    collectionList = it
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(CollectionListViewModel.TIMEOUT_MILLIS),
                initialValue = CollectionListUiState()
            )

    var dropdownMenuExpanded by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    fun toggleDropdownMenu() {
        dropdownMenuExpanded = !dropdownMenuExpanded

    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun collectionSatisfiesQuery(name: String): Boolean {
        return name.contains(searchQuery, ignoreCase = true)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val appRepository = application.container.appRepository
                CollectionListViewModel(
                    appRepository = appRepository
                )
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L
    }
}