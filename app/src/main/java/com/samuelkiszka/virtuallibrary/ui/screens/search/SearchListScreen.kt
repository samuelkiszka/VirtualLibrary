package com.samuelkiszka.virtuallibrary.ui.screens.search

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.enums.NavbarCurrentPosition
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import com.samuelkiszka.virtuallibrary.ui.components.AddNewEntityProposal
import com.samuelkiszka.virtuallibrary.ui.components.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.components.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.components.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.AddEditBookDestination

object SearchListDestination : NavigationDestination {
    override val route = "SearchList"
    override val titleRes = R.string.screen_search_list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: SearchListViewModel,
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.screen_search_list
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreen = NavbarCurrentPosition.SEARCH
            )
        }
    ) { innerPadding ->
        SearchListBody(
            viewModel = viewModel,
            onBookClicked = {
                navController.navigate(
                    "${AddEditBookDestination.route}/${it}"
                )
            },
            onAddButtonClicked = {
                navController.navigate(
                    AddEditBookDestination.route
                )
            },
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun SearchListBody(
    viewModel: SearchListViewModel,
    onBookClicked: (String) -> Unit,
    onAddButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        VirtualLibrarySearchBar(
            hintText = stringResource(id = R.string.search_hint_search_list),
            query = viewModel.query,
            onSearch = { viewModel.getBooksByQuery() },
            onQueryChange = { viewModel.updateQuery(it) }
        )
        when (viewModel.searchListUiState) {
            is SearchListUiState.Loading -> LoadingScreen()
            is SearchListUiState.Error -> AddNewEntityProposal(
                proposalText = stringResource(id = R.string.resource_search_book_error),
                onAddButtonClicked = onAddButtonClicked
            )
            is SearchListUiState.Success -> SearchBookList(
                bookList = (viewModel.searchListUiState as SearchListUiState.Success).bookList,
                onBookClicked = onBookClicked
            )
            is SearchListUiState.Empty -> AddNewEntityProposal(
                proposalText = stringResource(id = R.string.resource_search_book),
                onAddButtonClicked = onAddButtonClicked
            )
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun SearchBookList(
    bookList: List<BookApiModel>,
    onBookClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_around)
            )
    ) {
        items(bookList) {
            SearchBookListCard(
                book = it,
                modifier = Modifier
                    .clickable {
                        onBookClicked(it.getJsonUrlEncoded())
                    }
            )
        }
    }
}

@Composable
fun SearchBookListCard(
    book: BookApiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(id = R.dimen.padding_little))
            .height(dimensionResource(id = R.dimen.list_card_height))
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.cover.medium)
                    .crossfade(true)
                    .build(),
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                error = painterResource(id = R.drawable.demo_book_cover),
                placeholder = painterResource(id = R.drawable.demo_book_cover),
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.list_image_width))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_little))
            ){
                Text(
                    text = book.title
                )
                Text(
                    text = book.authors[0].name
                )
            }
        }

    }
}