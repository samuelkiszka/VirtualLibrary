package com.samuelkiszka.virtuallibrary.ui.screens.search

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination

object SearchListDestination : NavigationDestination {
    override val route = "SearchList"
    override val titleRes = R.string.search_list_screen_name
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
                screenTitleId = R.string.search_list_screen_name
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreenRoute = SearchListDestination.route
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
            query = viewModel.query,
            onSearch = { viewModel.getBooksByQuery() },
            onQueryChange = { viewModel.updateQuery(it) }
        )
        when (viewModel.searchListUiState) {
            is SearchListUiState.Loading -> LoadingScreen()
            is SearchListUiState.Error -> ErrorScreen()
            is SearchListUiState.Success -> SearchBookList(
                bookList = (viewModel.searchListUiState as SearchListUiState.Success).bookList,
                onBookClicked = onBookClicked
            )
            is SearchListUiState.Empty -> AddNewBookText(
                onAddButtonClicked = onAddButtonClicked
            )
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Loading...",
        modifier = modifier
    )
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Error...",
        modifier = modifier
    )
}

@Composable
fun AddNewBookText(
    onAddButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
         Text(
             text = stringResource(id = R.string.add_book_text),
             style = MaterialTheme.typography.bodyLarge,
             textAlign = TextAlign.Center,
             modifier = Modifier
                 .padding(
                     horizontal = dimensionResource(id = R.dimen.padding_medium)
                 ),

         )
        IconButton(
            onClick = {
                onAddButtonClicked()
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = stringResource(R.string.add_book_button),
                modifier = Modifier
                    .size(150.dp)
            )
        }
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
                    .data(book.cover?.medium)
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

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SearchListScreenPreview() {
//    VirtualLibraryTheme {
//        SearchListScreen()
//    }
//}