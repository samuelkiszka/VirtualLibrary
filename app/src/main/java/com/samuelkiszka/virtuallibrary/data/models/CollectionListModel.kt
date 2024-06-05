package com.samuelkiszka.virtuallibrary.data.models

import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListUiState

data class CollectionListModel(
    val id: Int,
    val name: String,
    val books: List<BookCollectionListModel>
) {
}
