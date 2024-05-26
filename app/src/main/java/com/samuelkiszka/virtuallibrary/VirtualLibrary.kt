package com.samuelkiszka.virtuallibrary

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.samuelkiszka.virtuallibrary.ui.navigation.VirtualLibraryNavGraph

@Composable
fun VirtualLibrary(
    navController: NavHostController = rememberNavController()
) {
    VirtualLibraryNavGraph(
        navController = navController
    )
}