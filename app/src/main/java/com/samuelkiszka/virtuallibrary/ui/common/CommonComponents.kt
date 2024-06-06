package com.samuelkiszka.virtuallibrary.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.samuelkiszka.virtuallibrary.data.models.BookListModel
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualLibrarySearchBar(
    query: String = "",
    onSearch: (String) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        placeholder = {
            Text("Search for books...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        content = {},
        active = false,
        onActiveChange = {},
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
            .fillMaxWidth()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualLibraryTopBar(
    modifier: Modifier = Modifier,
    screenTitleId: Int = 0,
    screenTitle: String? = null,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    haveOptions: Boolean = false,
    onOptionClick: () -> Unit = {},
    options: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = screenTitle ?: stringResource(screenTitleId),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        actions = {
            if (haveOptions) {
                IconButton(
                    onClick = onOptionClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.option_button)
                    )
                }
                options()
            }
        }
    )
}

@Composable
fun VirtualLibraryBottomBar(
    navController: NavHostController,
    currentScreenRoute: String = ""
) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate(LibraryListDestination.route) },
                modifier = Modifier
                    .padding(5.dp)
            ) {
                if (currentScreenRoute == LibraryListDestination.route) {
                    Text(
                        text = "Library",
                    )
                }
                else {
                    Text(
                        text = "Library",
                        color = MaterialTheme.colorScheme.secondary)
                }
            }
            Button(
                onClick = { navController.navigate(CollectionListDestination.route) },
                modifier = Modifier
                    .padding(5.dp)
            ) {
                if (currentScreenRoute == CollectionListDestination.route) {
                    Text(
                        text = "Collection"
                    )
                }
                else {
                    Text(
                        text = "Collection",
                        color = MaterialTheme.colorScheme.secondary)
                }
            }
            Button(
                onClick = { navController.navigate(SearchListDestination.route) },
                modifier = Modifier
                    .padding(5.dp)
            ) {
                if (currentScreenRoute == SearchListDestination.route) {
                    Text(
                        text = "Search"
                    )
                }
                else {
                    Text(
                        text = "Search",
                        color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun DefaultAlertDialog(
    showDialogue: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    if (showDialogue) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = "Example Icon")
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
fun AddNewEntityProposal(
    onAddButtonClicked: () -> Unit,
    proposalText: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = proposalText,
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
                contentDescription = stringResource(R.string.add_item_button),
                modifier = Modifier
                    .size(150.dp)
            )
        }
    }
}

@Composable
fun SaveButton(
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onSave() },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.save_button_text))
    }
}

@Composable
fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            .height(2.dp)
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_little),
            )
    )
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
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.coverUrl)
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
                    text = book.author
                )
            }
        }

    }
}