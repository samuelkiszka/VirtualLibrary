package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.samuelkiszka.virtuallibrary.data.models.AddEditCollectionModel
import com.samuelkiszka.virtuallibrary.ui.common.SaveButton
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object AddEditCollectionDestination : NavigationDestination {
    override val route = "AddEditCollection"
    override val titleRes = R.string.add_edit_collection_screen_name
    const val ARGS = "data"
    val routeWithArgs = "$route/{$ARGS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCollectionScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: AddEditCollectionViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.add_edit_book_screen_name,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() }
            )
        },
        bottomBar = {
            SaveButton(
                onSave = {
                    coroutineScope.launch {
                        val id = viewModel.saveCollection()
                        navController.popBackStack()
                        navController.navigate("${CollectionDetailDestination.route}/$id")
                    }
                }
            )
        }
    ) { innerPadding ->
        AddEditCollectionBody(
            viewModel = viewModel,
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = innerPadding.calculateTopPadding()
            )
        )
    }
}

@Composable
fun AddEditCollectionBody(
    viewModel: AddEditCollectionViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        ValueEditingFields(
            viewModel
        )
    }
}

@Composable
fun ValueEditingFields(
    viewModel: AddEditCollectionViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = viewModel.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Title") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text("Author") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}