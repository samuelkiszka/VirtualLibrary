package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samuelkiszka.virtuallibrary.VirtualLibraryApplication
import com.samuelkiszka.virtuallibrary.data.AppRepository
import com.samuelkiszka.virtuallibrary.data.mocks.CollectionListModelMock
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel

data class CollectionListUiState(
    val collectionList: List<CollectionListModel> = emptyList()
)

class CollectionListViewModel(
    val appRepository: AppRepository,
): ViewModel() {

    var uiState by mutableStateOf(CollectionListUiState())
        private set

    fun getCollectionList() : List<CollectionListModel> {
        return CollectionListModelMock().getCollectionList()
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
    }
}