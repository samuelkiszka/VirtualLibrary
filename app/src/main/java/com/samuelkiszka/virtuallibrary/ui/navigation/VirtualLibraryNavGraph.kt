package com.samuelkiszka.virtuallibrary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.samuelkiszka.virtuallibrary.ui.screens.collection.AddEditCollectionDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.AddEditCollectionScreen
import com.samuelkiszka.virtuallibrary.ui.screens.collection.AddEditCollectionViewModel
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailViewModel
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListScreen
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListViewModel
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailScreen
import com.samuelkiszka.virtuallibrary.ui.screens.library.LibraryDetailViewModel
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
            route = LibraryDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(LibraryDetailDestination.ARGS) {
                type = NavType.LongType
            })
        ) {
            val viewModel: LibraryDetailViewModel = viewModel(factory = LibraryDetailViewModel.Factory)
            LibraryDetailScreen(
                navController = navController
            )
        }
        composable(
            route = CollectionListDestination.route
        ) {
            val viewModel: CollectionListViewModel = viewModel(factory = CollectionListViewModel.Factory)
            CollectionListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(
            route = CollectionDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(CollectionDetailDestination.ARGS) {
                type = NavType.LongType
            })
        ) {
            val viewModel: CollectionDetailViewModel = viewModel(factory = CollectionDetailViewModel.Factory)
            CollectionDetailScreen(
                navController = navController
            )
        }
        composable(
            route = AddEditCollectionDestination.routeWithArgs,
            arguments = listOf(navArgument(AddEditCollectionDestination.ARGS) {
                type = NavType.LongType
            })
        ) {
            val viewModel: AddEditCollectionViewModel = viewModel(factory = AddEditCollectionViewModel.Factory)
            AddEditCollectionScreen(
                navController = navController,
                viewModel = viewModel
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