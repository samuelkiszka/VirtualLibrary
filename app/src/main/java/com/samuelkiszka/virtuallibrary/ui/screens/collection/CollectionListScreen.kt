package com.samuelkiszka.virtuallibrary.ui.screens.collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.samuelkiszka.virtuallibrary.data.models.BookCollectionListModel
import com.samuelkiszka.virtuallibrary.data.models.CollectionListModel
import com.samuelkiszka.virtuallibrary.ui.common.AddNewEntityProposal
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibrarySearchBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailDestination

object CollectionListDestination : NavigationDestination {
    override val route = "CollectionList"
    override val titleRes = R.string.collection_list_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: CollectionListViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.collection_list_screen_name,
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
                                     text = "Add collection",
                                     style = MaterialTheme.typography.titleMedium
                                 )
                             },
                             onClick = {
                                 viewModel.toggleDropdownMenu()
                                 navController.navigate(
                                     AddEditCollectionDestination.route
                                 )
                             }
                         )
                     }
                }
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreenRoute = CollectionListDestination.route
            )
        }
    ) { innerPadding ->
        CollectionListBody(
            collections = uiState.value.collectionList,
            onBookClicked = {
                navController.navigate("${LibraryDetailDestination.route}/$it")
            },
            onCollectionClicked = {
                navController.navigate("${CollectionDetailDestination.route}/$it")
            },
            onNewCollectionClicked = {
                navController.navigate(
                    AddEditCollectionDestination.route
                )
            },
            searchQuery = viewModel.searchQuery,
            updateQuery = viewModel::updateSearchQuery,
            collectionSatisfiesQuery = viewModel::collectionSatisfiesQuery,
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun CollectionListBody(
    collections: List<CollectionListModel>,
    onBookClicked: (Long) -> Unit,
    onCollectionClicked: (Long) -> Unit,
    onNewCollectionClicked: (Int) -> Unit,
    searchQuery: String,
    updateQuery: (String) -> Unit,
    collectionSatisfiesQuery: (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier.fillMaxSize()
    ) {
        VirtualLibrarySearchBar(
            hintText = stringResource(id = R.string.collection_list_search_hint),
            query = searchQuery,
            onQueryChange = { updateQuery(it) }
        )
        if (collections.isEmpty()) {
            AddNewEntityProposal(
                onAddButtonClicked = { onNewCollectionClicked(-1) },
                proposalText = stringResource(R.string.add_collection_text)
            )
        }
        CollectionList(
            collections = collections,
            onBookClicked = onBookClicked,
            onCollectionClicked = onCollectionClicked,
            collectionSatisfiesQuery = collectionSatisfiesQuery
        )
    }
}

@Composable
fun CollectionList(
    collections: List<CollectionListModel>,
    onBookClicked: (Long) -> Unit,
    onCollectionClicked: (Long) -> Unit,
    collectionSatisfiesQuery: (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_around))
    ) {
        items(collections) {
            if (collectionSatisfiesQuery(it.name)) {
                CollectionCard(
                    it,
                    onBookClicked = onBookClicked,
                    onCollectionClicked = onCollectionClicked
                )
            }
        }
    }
}

@Composable
fun CollectionCard(
    collection: CollectionListModel,
    modifier: Modifier = Modifier,
    onBookClicked: (Long) -> Unit,
    onCollectionClicked: (Long) -> Unit
) {
    Column(
        modifier = modifier
            .height(200.dp)
            .padding(
                bottom = dimensionResource(id = R.dimen.padding_around)
            )
            .clickable {
                onCollectionClicked(collection.id)
            }
    ) {
        Text(
            text = collection.name,
            modifier = modifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_around))
        )
        LazyRow(
            modifier = modifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_around))
        ) {
            items(collection.books) {
                BookCard(
                    it,
                    onBookClicked = onBookClicked
                )
            }
        }
    }
}

@Composable
fun BookCard(
    book: BookCollectionListModel,
    modifier: Modifier = Modifier,
    onBookClicked: (Long) -> Unit
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(book.coverUrl)
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.demo_book_cover),
        placeholder = painterResource(id = R.drawable.demo_book_cover),
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(id = R.dimen.padding_little),
                end = dimensionResource(id = R.dimen.padding_little)
            )
            .clickable {
                onBookClicked(book.id)
            }
    )
}