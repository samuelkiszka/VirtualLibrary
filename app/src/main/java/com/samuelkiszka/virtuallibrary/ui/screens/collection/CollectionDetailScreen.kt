package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
import com.samuelkiszka.virtuallibrary.ui.common.AddNewEntityProposal
import com.samuelkiszka.virtuallibrary.ui.common.BookListCard
import com.samuelkiszka.virtuallibrary.ui.common.DefaultAlertDialog
import com.samuelkiszka.virtuallibrary.ui.common.MembershipDialog
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListDestination


object CollectionDetailDestination : NavigationDestination {
    override val route = "CollectionDetail"
    override val titleRes = R.string.screen_collection_detail
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
                                    text = stringResource(R.string.dropdown_option_manage_books),
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
                                    text = stringResource(R.string.dropdown_option_edit_collection),
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
                                    text = stringResource(R.string.dropdown_option_delete_collection),
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
        dialogTitle = stringResource(id = R.string.alert_delete_collection_title),
        dialogText = stringResource(id = R.string.alert_delete_collection_text).format(viewModel.uiState.collection.name),
        icon = Icons.Filled.Delete
    )
    MembershipDialog(
        showAddBookDialog = viewModel.showManageBooksDialog,
        addMembersOption = viewModel.addBooksOption,
        showItemsToAdd = viewModel::showItemsToAdd,
        toggleManageMembershipDialog = viewModel::toggleAddBookDialog,
        others = booksNotInCollection.value,
        members = collectionBooks.value.map {
            AddListItemModel(
                id = it.id,
                title = it.title
            )
        },
        addMember = viewModel::addBookToCollection,
        removeMember = viewModel::removeBookFromCollection,
        addText = stringResource(id = R.string.button_add_books_membership),
        removeText = stringResource(id = R.string.button_remove_books_membership),
        noMemberToAddText = stringResource(id = R.string.resource_add_book_to_collection_no_more_books),
        noMemberToRemoveText = stringResource(id = R.string.resource_add_book_to_collection),
        navigateToAddMembers = navigateToAddBook
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
                proposalText = stringResource(id = R.string.resource_add_book_to_collection)
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