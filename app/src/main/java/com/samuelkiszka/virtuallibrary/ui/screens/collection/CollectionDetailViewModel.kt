package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samuelkiszka.virtuallibrary.VirtualLibraryApplication
import com.samuelkiszka.virtuallibrary.data.AppRepository

class CollectionDetailViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var args: Long? = savedStateHandle[CollectionDetailDestination.ARGS]

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