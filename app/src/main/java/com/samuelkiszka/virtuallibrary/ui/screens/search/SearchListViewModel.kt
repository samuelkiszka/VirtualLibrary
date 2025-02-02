package com.samuelkiszka.virtuallibrary.ui.screens.search

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
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import kotlinx.coroutines.launch

sealed interface SearchListUiState {
    data class Success(val bookList: List<BookApiModel>): SearchListUiState
    data object Error: SearchListUiState
    data object Loading: SearchListUiState
    data object Empty: SearchListUiState
}

class SearchListViewModel(
    private val appRepository: AppRepository
): ViewModel() {
    var searchListUiState: SearchListUiState by mutableStateOf(SearchListUiState.Empty)
        private set
    var query by mutableStateOf("")
        private set

    init {
        getBooksByQuery()
    }

    fun updateQuery(newQuery: String) {
        this.query = newQuery
    }

    fun getBooksByQuery() {
        if (query.isEmpty()) {
            searchListUiState = SearchListUiState.Empty
            return
        }
        viewModelScope.launch {
            searchListUiState = SearchListUiState.Loading
            searchListUiState = try {
                SearchListUiState.Success(appRepository.getBooksByQuery(query))
            } catch (e: Exception) {
                SearchListUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.appRepository
                SearchListViewModel(appRepository = bookRepository)
            }
        }
    }
}