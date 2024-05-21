package com.samuelkiszka.virtuallibrary.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object CollectionListDestination : NavigationDestination {
    override val route = "CollectionList"
    override val titleRes = R.string.collection_list_screen_name
}

@Composable
fun CollectionListScreen() {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        CollectionList(
            collections = CollectionListViewModel().getCollectionList()
        )
    }
}

@Composable
fun CollectionList(
    collections: List<CollectionListModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
    ) {
        items(collections) {
            CollectionCard(it)
        }
    }
}

@Composable
fun CollectionCard(
    collection: CollectionListModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(200.dp)
            .padding(
                bottom = dimensionResource(id = R.dimen.padding_around)
            )
    ) {
        Text(
            text = collection.name,
            modifier = modifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_around))
        )
        LazyRow(
            modifier = modifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_around))
        ) {
            items(collection.books) {
                BookCard(it)
            }
        }
    }
}

@Composable
fun BookCard(
    book: BookCollectionListModel,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(id = book.image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(id = R.dimen.padding_little),
                end = dimensionResource(id = R.dimen.padding_little)
            )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectionListScreenPreview() {
    VirtualLibraryTheme {
        CollectionListScreen()
    }
}