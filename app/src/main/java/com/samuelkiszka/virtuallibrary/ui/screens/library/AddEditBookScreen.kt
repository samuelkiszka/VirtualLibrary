package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.common.SaveButton
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object AddEditBookDestination : NavigationDestination {
    override val route = "AddEditBook"
    override val titleRes = R.string.screen_add_edit_book
    const val ARGS = "data"
    val routeWithArgs = "$route/{$ARGS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: AddEditBookViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = if (viewModel.id != 0) R.string.screen_add_edit_book_edit else R.string.screen_add_edit_book_add,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() }
            )
        },
        bottomBar = {
            SaveButton(
                onSave = {
                    coroutineScope.launch {
                        val id = viewModel.saveBook()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate("${LibraryDetailDestination.route}/$id")
                    }
                }
            )
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
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
            .fillMaxSize()
            .verticalScroll(scrollState)
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
                .data(viewModel.coverUrl)
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
            label = { Text(stringResource(R.string.label_title)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.author,
            onValueChange = { viewModel.updateAuthor(it) },
            label = { Text(stringResource(R.string.label_author)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.yearPublished,
            onValueChange = { viewModel.updateYearPublished(it) },
            label = { Text(stringResource(R.string.label_year_published)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.numberOfPages,
            onValueChange = { viewModel.updateNumberOfPages(it) },
            label = { Text(stringResource(R.string.label_number_of_pages)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
