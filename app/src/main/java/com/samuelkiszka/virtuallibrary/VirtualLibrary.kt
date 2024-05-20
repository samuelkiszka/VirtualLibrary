package com.samuelkiszka.virtuallibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.samuelkiszka.virtuallibrary.ui.screens.CollectionDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.CollectionDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.CollectionListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.LibraryDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.LibraryDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.LibraryListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.LibraryListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.SearchDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.SearchDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.SearchListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.SearchListScreen
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTypography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualLibraryTopBar(
    route: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
            )
    ) {
//        Text(
//            text = route,
//            style = VirtualLibraryTypography.headlineSmall,
//            color = MaterialTheme.colorScheme.secondary,
//            modifier = Modifier
//                .padding(
//                    top = 30.dp,
//                    start = dimensionResource(id = R.dimen.padding_around)
//                )
//        )
        SearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            placeholder = {
                Text("Search for books...")
            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    tint = MaterialTheme.colorScheme.onSurface,
//                    contentDescription = null
//                )
//            },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.List,
//                    tint = MaterialTheme.colorScheme.onSurface,
//                    contentDescription = null
//                )
//            },
            content = {},
            active = false,
            onActiveChange = {},
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_around))
                .fillMaxWidth()
        )
    }
}

@Composable
fun VirtualLibraryBottomBar(
    navController: NavHostController,
    currentScreenRoute: String
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
fun VirtualLibrary(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreenRoute = backStackEntry?.destination?.route ?: LibraryListDestination.route

    Scaffold(
        topBar = {
            VirtualLibraryTopBar(route = currentScreenRoute)
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController,
                currentScreenRoute = currentScreenRoute
                )
        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LibraryListDestination.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = LibraryListDestination.route
            ) {
                LibraryListScreen(
                    onItemClicked = {
                        navController.navigate(LibraryDetailDestination.route)
                    }
                )
            }
            composable(
                route = LibraryDetailDestination.route
            ) {
                LibraryDetailScreen()
            }
            composable(
                route = CollectionListDestination.route
            ) {
                CollectionListScreen()
            }
            composable(
                route = CollectionDetailDestination.route
            ) {
                CollectionDetailScreen()
            }
            composable(
                route = SearchListDestination.route
            ) {
                SearchListScreen()
            }
            composable(
                route = SearchDetailDestination.route
            ) {
                SearchDetailScreen()
            }
        }
    }
}