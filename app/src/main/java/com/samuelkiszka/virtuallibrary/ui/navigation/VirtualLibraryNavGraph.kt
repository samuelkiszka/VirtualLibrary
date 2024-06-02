package com.samuelkiszka.virtuallibrary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.search.AddEditBookDestination
import com.samuelkiszka.virtuallibrary.ui.screens.search.AddEditBookScreen
import com.samuelkiszka.virtuallibrary.ui.screens.search.AddEditBookViewModel
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
                viewModel = viewModel
            )
        }
        composable(
            route = AddEditBookDestination.route
        ) {
            val viewModel: AddEditBookViewModel = viewModel(factory = AddEditBookViewModel.Factory)
            AddEditBookScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(
            route = AddEditBookDestination.routeWithArgs,
            arguments = listOf(navArgument(AddEditBookDestination.ARGS) {
                type = NavType.StringType
            })
        ) {
            val viewModel: AddEditBookViewModel = viewModel(factory = AddEditBookViewModel.Factory)
            viewModel.populateData()
            AddEditBookScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}