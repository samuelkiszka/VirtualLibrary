package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object CollectionListDestination : NavigationDestination {
    override val route = "CollectionList"
    override val titleRes = R.string.collection_list_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListScreen(
    navController: NavHostController = NavHostController(LocalContext.current)
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.collection_list_screen_name
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreenRoute = CollectionListDestination.route
            )
        }
    ) { innerPadding ->
        CollectionListBody(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun CollectionListBody(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.fillMaxSize()
    ) {
        VirtualLibrarySearchBar()
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
            .padding(horizontal = dimensionResource(id = R.dimen.padding_around))
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