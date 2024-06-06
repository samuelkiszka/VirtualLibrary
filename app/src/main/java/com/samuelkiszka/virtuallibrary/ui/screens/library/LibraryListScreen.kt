package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.ui.common.BookListCard
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object LibraryListDestination : NavigationDestination {
    override val route = "LibraryList"
    override val titleRes = R.string.library_list_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryListScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: LibraryListViewModel = viewModel(factory = LibraryListViewModel.Factory),
) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.library_list_screen_name
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreenRoute = LibraryListDestination.route
            )
        }
    ) { innerPadding ->
        LibraryListBody(
            books = uiState.value.bookList,
            onItemClicked = {
                navController.navigate("${LibraryDetailDestination.route}/$it")
            },
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun LibraryListBody(
    books: List<BookListModel>,
    onItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        VirtualLibrarySearchBar {}
        BookList(
            books = books,
            onItemClicked = onItemClicked,
            modifier = Modifier
        )
    }
}

@Composable
fun BookList(
    books: List<BookListModel>,
    onItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_around)
            )
    ) {
        items(books) {
            BookListCard(
                book = it,
                modifier = Modifier
                    .clickable {
                        onItemClicked(it.id)
                    }
            )
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryListScreenPreview() {
    VirtualLibraryTheme {
        LibraryListScreen()
    }
}
