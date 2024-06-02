package com.samuelkiszka.virtuallibrary.ui.screens.search

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination

object AddEditBookDestination : NavigationDestination {
    override val route = "AddEditBook"
    override val titleRes = R.string.add_edit_book_screen_name
    const val ARGS = "data"
    val routeWithArgs = "$route/{$ARGS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: AddEditBookViewModel
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.add_edit_book_screen_name,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() }
            )
        },
        bottomBar = {
            SaveButton()
        }
    ) { innerPadding ->
        SearchDetailBody(
            viewModel = viewModel,
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = innerPadding.calculateTopPadding()
            )
        )
    }
}

@Composable
fun SearchDetailBody(
    viewModel: AddEditBookViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
            .fillMaxSize()
    ) {
        Header(
            viewModel
        )
        ValueEditingFields(
            viewModel
        )
    }
}

@Composable
fun Header(
    viewModel: AddEditBookViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(viewModel.addEditUiState.book.coverUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            error = painterResource(id = R.drawable.demo_book_cover),
            placeholder = painterResource(id = R.drawable.demo_book_cover),
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_around))
                .width(150.dp)
        )
        Column(
            modifier = Modifier
        ) {
            Text(
                text = viewModel.title,
            )
            Text(
                text = viewModel.author,
            )
        }
    }
}

@Composable
fun ValueEditingFields(
    viewModel: AddEditBookViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = viewModel.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.author,
            onValueChange = { viewModel.updateAuthor(it) },
            label = { Text("Author") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.yearPublished,
            onValueChange = { viewModel.updateYearPublished(it) },
            label = { Text("Year published") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.numberOfPages.toString(),
            onValueChange = { viewModel.updateNumberOfPages(it) },
            label = { Text("Number of pages") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.notes,
            onValueChange = { viewModel.updateNotes(it) },
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Save")
    }
}