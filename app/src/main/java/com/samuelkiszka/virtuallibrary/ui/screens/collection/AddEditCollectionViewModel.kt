package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.samuelkiszka.virtuallibrary.data.AppRepository
import com.samuelkiszka.virtuallibrary.data.models.AddEditCollectionModel

data class AddEditUiState(
    var collection: AddEditCollectionModel = AddEditCollectionModel(),
)

class AddEditCollectionViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var addEditUiState: AddEditUiState by mutableStateOf(AddEditUiState())
        private set

    var args: String? = savedStateHandle[AddEditCollectionDestination.ARGS]

    var id by mutableIntStateOf(0)
        private set

    var name by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    fun updateId(newId: Int) {
        id = newId
    }

    fun updateName(newTitle: String) {
        name = newTitle
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun populateData() {
        if (!args.isNullOrEmpty()){
            addEditUiState.collection = AddEditCollectionModel().fromJson(args!!)
        }
        updateId(addEditUiState.collection.id)
        updateName(addEditUiState.collection.name)
        updateDescription(addEditUiState.collection.description)
    }

    suspend fun saveCollection(): Long {
        val entity = AddEditCollectionModel(
            id = id,
            name = name,
            description = description
        ).toCollectionEntity()
        if (id != 0) {
            appRepository.updateCollection(entity)
            return entity.id
        } else {
            return appRepository.addCollection(entity)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VirtualLibraryApplication)
                val bookRepository = application.container.appRepository
                AddEditCollectionViewModel(
                    appRepository = bookRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}
