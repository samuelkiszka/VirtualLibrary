package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.common.DefaultAlertDialog
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.AddEditBookDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object CollectionDetailDestination : NavigationDestination {
    override val route = "CollectionDetail"
    override val titleRes = R.string.collection_detail_screen_name
    const val ARGS = "id"
    val routeWithArgs = "${CollectionDetailDestination.route}/{$ARGS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: CollectionDetailViewModel
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitle = viewModel.uiState.collection.name,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() },
                haveOptions = true,
                onOptionClick = viewModel::toggleDropdownMenu,
                options = {
                    DropdownMenu(
                        expanded = viewModel.dropdownMenuExpanded,
                        onDismissRequest = viewModel::toggleDropdownMenu
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            onClick = {
                                viewModel.toggleDropdownMenu()
                                navController.navigate("${AddEditCollectionDestination.route}/${viewModel.uiState.collection.getJsonUrlEncoded()}")
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            onClick = {
                                viewModel.toggleDropdownMenu()
                                viewModel.toggleDeleteAlert()
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->
        CollectionDetailBody(
            viewModel = viewModel,
            navigateUp = { navController.navigateUp() },
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun CollectionDetailBody(
    viewModel: CollectionDetailViewModel,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    DefaultAlertDialog(
        showDialogue = viewModel.showDeleteAlert,
        onDismissRequest = viewModel::toggleDeleteAlert,
        onConfirmation = {
            viewModel.deleteCollection()
            viewModel.toggleDeleteAlert()
            navigateUp()
        },
        dialogTitle = stringResource(id = R.string.collection_delete_title),
        dialogText = stringResource(id = R.string.collection_delete_text).format(viewModel.uiState.collection.name),
        icon = Icons.Filled.Delete
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = viewModel.uiState.collection.description,
        )
    }
}