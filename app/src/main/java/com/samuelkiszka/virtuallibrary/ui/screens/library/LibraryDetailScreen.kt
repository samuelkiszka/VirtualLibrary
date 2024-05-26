package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object LibraryDetailDestination : NavigationDestination {
    override val route = "LibraryDetail"
    override val titleRes = R.string.library_detail_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryDetailScreen(
    navController: NavHostController = NavHostController(LocalContext.current)
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.library_detail_screen_name,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() }
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        LibraryDetailBody(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun LibraryDetailBody(
    modifier: Modifier = Modifier
) {
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryDetailScreenPreview() {
    VirtualLibraryTheme {
        LibraryDetailScreen()
    }
}