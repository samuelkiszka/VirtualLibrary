package com.samuelkiszka.virtuallibrary.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookApiModel
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.BookList
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListViewModel
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object SearchListDestination : NavigationDestination {
    override val route = "SearchList"
    override val titleRes = R.string.search_list_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    searchListUiState: SearchListUiState
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
            searchListUiState = searchListUiState,
            onItemClicked = {
                navController.navigate(SearchDetailDestination.route)
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
    searchListUiState: SearchListUiState,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    VirtualLibrarySearchBar()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        when (searchListUiState) {
            is SearchListUiState.Loading -> LoadingScreen()
            is SearchListUiState.Error -> ErrorScreen()
            is SearchListUiState.Success -> SearchBookList(
                bookList = searchListUiState.bookList
            )
            is SearchListUiState.Empty -> AddNewBookText(
                onButtonClicked = onItemClicked
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
    onButtonClicked: (String) -> Unit,
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
                onButtonClicked("0")
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
) {
    Text(
        text = bookList.first().toString()
    )
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SearchListScreenPreview() {
//    VirtualLibraryTheme {
//        SearchListScreen()
//    }
//}