package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.enums.NavbarCurrentPosition
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.ui.components.AddNewEntityProposal
import com.samuelkiszka.virtuallibrary.ui.components.BookListCard
import com.samuelkiszka.virtuallibrary.ui.components.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.components.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.components.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object LibraryListDestination : NavigationDestination {
    override val route = "LibraryList"
    override val titleRes = R.string.screen_library_list
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
                screenTitleId = R.string.screen_library_list
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreen = NavbarCurrentPosition.LIBRARY
            )
        }
    ) { innerPadding ->
        LibraryListBody(
            books = uiState.value.bookList,
            onItemClicked = {
                navController.navigate("${LibraryDetailDestination.route}/$it")
            },
            searchQuery = viewModel.searchQuery,
            updateSearchQuery = viewModel::updateSearchQuery,
            bookSatisfiesQuery = viewModel::bookSatisfiesQuery,
            onNewBookClicked = {
                navController.navigate(SearchListDestination.route)
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
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    bookSatisfiesQuery: (BookListModel) -> Boolean,
    onNewBookClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        VirtualLibrarySearchBar(
            hintText = stringResource(id = R.string.search_hint_library_list),
            query = searchQuery,
            onQueryChange = { updateSearchQuery(it) },
        )
        if (books.isEmpty()) {
            AddNewEntityProposal(
                onAddButtonClicked = onNewBookClicked,
                proposalText = stringResource(R.string.resource_add_books)
            )
        }
        BookList(
            books = books,
            onItemClicked = onItemClicked,
            bookSatisfiesQuery = bookSatisfiesQuery,
            modifier = Modifier
        )
    }
}

@Composable
fun BookList(
    books: List<BookListModel>,
    onItemClicked: (Long) -> Unit,
    bookSatisfiesQuery: (BookListModel) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_around)
            )
    ) {
        items(books) {
            if (bookSatisfiesQuery(it)) {
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
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryListScreenPreview() {
    VirtualLibraryTheme {
        LibraryListScreen()
    }
}
