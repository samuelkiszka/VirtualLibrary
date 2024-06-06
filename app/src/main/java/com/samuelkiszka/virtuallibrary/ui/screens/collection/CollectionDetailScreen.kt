package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
import com.samuelkiszka.virtuallibrary.ui.common.AddNewEntityProposal
import com.samuelkiszka.virtuallibrary.ui.common.BookListCard
import com.samuelkiszka.virtuallibrary.ui.common.DefaultAlertDialog
import com.samuelkiszka.virtuallibrary.ui.common.Divider
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListDestination


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
                                    text = "Manage books",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            onClick = {
                                viewModel.toggleDropdownMenu()
                                viewModel.toggleAddBookDialog()
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit collection",
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
                                    text = "Delete collection",
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
            navigateToAddBook = {
                navController.navigate(SearchListDestination.route)
                viewModel.toggleAddBookDialog()
            },
            onBookClicked = {
                navController.navigate("${LibraryDetailDestination.route}/$it")
            },
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = innerPadding.calculateTopPadding(),
                start = dimensionResource(id = R.dimen.padding_around),
                end = dimensionResource(id = R.dimen.padding_around),
            )
        )
    }
}

@Composable
fun CollectionDetailBody(
    viewModel: CollectionDetailViewModel,
    navigateUp: () -> Unit,
    navigateToAddBook: () -> Unit,
    onBookClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val collectionBooks = viewModel.booksInCollection.collectAsState()
    val booksNotInCollection = viewModel.booksNotInCollection.collectAsState()
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
    MembershipDialog(
        showAddBookDialog = viewModel.showAddBookDialog,
        addBooksOption = viewModel.addBooksOption,
        showItemsToAdd = viewModel::showItemsToAdd,
        toggleAddBookDialog = viewModel::toggleAddBookDialog,
        members = booksNotInCollection.value,
        others = collectionBooks.value.map {
            AddListItemModel(
                id = it.id,
                title = it.title
            )
        },
        addItem = viewModel::addBookToCollection,
        removeItem = viewModel::removeBookFromCollection,
        navigateToAddBook = navigateToAddBook
    )
    Column(
        modifier = modifier
    ) {
        Text(
            text = viewModel.uiState.collection.description,
        )
        if (collectionBooks.value.isEmpty()) {
            AddNewEntityProposal(
                onAddButtonClicked = {
                    viewModel.toggleAddBookDialog()
                    viewModel.showItemsToAdd(true)
                },
                proposalText = stringResource(id = R.string.add_book_to_collection_text)
            )
        }
        else {
            LazyColumn {
                items(collectionBooks.value) {
                    BookListCard(
                        book = it,
                        modifier = Modifier
                            .clickable {
                                onBookClicked(it.id)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun MembershipDialog(
    showAddBookDialog: Boolean,
    addBooksOption: Boolean = true,
    showItemsToAdd: (Boolean) -> Unit,
    toggleAddBookDialog: () -> Unit,
    members: List<AddListItemModel>,
    others: List<AddListItemModel>,
    addItem: (Long) -> Unit,
    removeItem: (Long) -> Unit,
    navigateToAddBook: () -> Unit
) {
    if (showAddBookDialog) {
        Dialog(
            onDismissRequest = toggleAddBookDialog
        ) {
            Card(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                showItemsToAdd(true)
                            }
                            .background(
                                color = if (addBooksOption) CardDefaults.cardColors().containerColor else MaterialTheme.colorScheme.background.copy(
                                    alpha = 0.5f
                                )
                            )
                            .weight(1f)
                    ) {
                        Text(
                            text = "Add books",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(
                                    dimensionResource(id = R.dimen.padding_medium)
                                )
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                showItemsToAdd(false)
                            }
                            .background(
                                color = if (!addBooksOption) CardDefaults.cardColors().containerColor else MaterialTheme.colorScheme.background.copy(
                                    alpha = 0.5f
                                )
                            )
                            .weight(1f)
                    ) {
                        Text(
                            text = "Remove books",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(
                                    dimensionResource(id = R.dimen.padding_medium)
                                )
                        )
                    }

                }
                if (addBooksOption) {
                    MembersList(
                        members = members,
                        onItemClicked = addItem,
                    ) {
                        AddNewEntityProposal(
                            onAddButtonClicked = navigateToAddBook,
                            proposalText = stringResource(R.string.add_books_to_collection_no_more_books_text)
                        )
                    }
                }
                else {
                    MembersList(
                        members = others,
                        onItemClicked = removeItem,
                    ) {
                        AddNewEntityProposal(
                            onAddButtonClicked = {
                                showItemsToAdd(true)
                            },
                            proposalText = stringResource(id = R.string.add_book_to_collection_text)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MembersList(
    members: List<AddListItemModel>,
    onItemClicked: (Long) -> Unit,
    onMembersEmpty: @Composable () -> Unit,
) {
    if (members.isEmpty()) {
        onMembersEmpty()
    }
    else {
        LazyColumn {
            items(members) {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.padding_extra_little)
                        )
                        .fillMaxWidth()
                        .clickable {
                            onItemClicked(it.id)
                        }
                )
            }
        }
    }
}