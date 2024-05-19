package com.samuelkiszka.virtuallibrary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualLibraryTopBar(
    route: String
) {
    TopAppBar(title = { Text(text = route) })
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
                LibraryListScreen()
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