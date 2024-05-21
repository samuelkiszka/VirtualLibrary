package com.samuelkiszka.virtuallibrary.ui.screens

import androidx.lifecycle.ViewModel
import com.samuelkiszka.virtuallibrary.data.mocks.CollectionListModelMock
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel

class CollectionListViewModel: ViewModel() {
    fun getCollectionList() : List<CollectionListModel> {
        return CollectionListModelMock().getCollectionList()
    }
}