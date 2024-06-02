package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.entities.BookEntity
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object LibraryDetailDestination : NavigationDestination {
    override val route = "LibraryDetail"
    override val titleRes = R.string.library_detail_screen_name
    const val ARGS = "id"
    val routeWithArgs = "$route/{$ARGS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryDetailScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: LibraryDetailViewModel = viewModel(factory = LibraryDetailViewModel.Factory),
) {
    val uiState = viewModel.uiState.collectAsState()
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
            book = uiState.value.book,
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun LibraryDetailBody(
    book: BookEntity,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ){
        Text(
            text = book.title,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryDetailScreenPreview() {
    VirtualLibraryTheme {
        LibraryDetailScreen()
    }
}