package com.samuelkiszka.virtuallibrary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object LibraryListDestination : NavigationDestination {
    override val route = "LibraryList"
    override val titleRes = R.string.library_list_screen_name
}

@Composable
fun LibraryListScreen(
    onItemClicked: (String) -> Unit,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_around))
    ) {
        BookList(
            books = LibraryListViewModel().getBookList(),
            onItemClicked = onItemClicked,
            modifier = Modifier
        )
    }
}

@Composable
fun BookList(
    books: List<BookListModel>,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(books) {
            BookListCard(
                book = it,
                modifier = Modifier
                    .clickable {
                        onItemClicked(it.title)
                    }
            )
        }
    }
}

@Composable
fun BookListCard(
    book: BookListModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(id = R.dimen.padding_little))
            .height(110.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = book.image),
                contentDescription = "",
                modifier = Modifier
//                    .height(150.dp)
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
                    text = book.author
                )
            }
        }

    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryListScreenPreview() {
    VirtualLibraryTheme {
        LibraryListScreen({})
    }
}
