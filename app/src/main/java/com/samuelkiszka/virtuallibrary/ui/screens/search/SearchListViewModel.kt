package com.samuelkiszka.virtuallibrary.ui.screens.search

import android.util.Log
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
import com.samuelkiszka.virtuallibrary.data.BookRepository
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import kotlinx.coroutines.launch

sealed interface SearchListUiState {
    data class Success(val bookList: List<BookApiModel>): SearchListUiState
    object Error: SearchListUiState
    object Loading: SearchListUiState
    object Empty: SearchListUiState
}

class SearchListViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    var searchListUiState: SearchListUiState by mutableStateOf(SearchListUiState.Loading)
        private set
    val queryText: String = "harry"

    init {
        getIsbnList(queryText)
    }

    fun getIsbnList(query: String) {
        if (query.isEmpty()) {
            searchListUiState = SearchListUiState.Empty
            return
        }
        viewModelScope.launch {
            searchListUiState = SearchListUiState.Loading
            searchListUiState = try {
                SearchListUiState.Success(bookRepository.getBooksByQuery(query))
            } catch (e: Exception) {
                Log.e("BookRepository", "getIsbnList: ", e)
                SearchListUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.bookRepository
                SearchListViewModel(bookRepository = bookRepository)
            }
        }
    }
}