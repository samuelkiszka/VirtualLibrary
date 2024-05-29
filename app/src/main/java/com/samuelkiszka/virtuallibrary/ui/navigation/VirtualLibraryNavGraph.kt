package com.samuelkiszka.virtuallibrary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.search.SearchListViewModel

@Composable
fun VirtualLibraryNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LibraryListDestination.route,
        modifier = modifier
    ) {
        composable(
            route = LibraryListDestination.route
        ) {
            LibraryListScreen(
                navController = navController
            )
        }
        composable(
            route = LibraryDetailDestination.route
        ) {
            LibraryDetailScreen(
                navController = navController
            )
        }
        composable(
            route = CollectionListDestination.route
        ) {
            CollectionListScreen(
                navController = navController
            )
        }
        composable(
            route = CollectionDetailDestination.route
        ) {
            CollectionDetailScreen(
                navController = navController
            )
        }
        composable(
            route = SearchListDestination.route
        ) {
            val viewModel: SearchListViewModel = viewModel(factory = SearchListViewModel.Factory)
            SearchListScreen(
                navController = navController,
                searchListUiState = viewModel.searchListUiState
            )
        }
        composable(
            route = SearchDetailDestination.route
        ) {
            SearchDetailScreen(
                navController = navController
            )
        }
    }
}