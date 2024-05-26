package com.samuelkiszka.virtuallibrary.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualLibrarySearchBar() {
    SearchBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
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
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.List,
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
    screenTitleId: Int,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(screenTitleId),
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
        modifier = modifier
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