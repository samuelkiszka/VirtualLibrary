package com.samuelkiszka.virtuallibrary.ui.screens.collection

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

object CollectionDetailDestination : NavigationDestination {
    override val route = "CollectionDetail"
    override val titleRes = R.string.collection_detail_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    navController: NavHostController = NavHostController(LocalContext.current)
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.collection_detail_screen_name
            )
        },
        bottomBar = {
            VirtualLibraryBottomBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        CollectionDetailBody(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = dimensionResource(id = R.dimen.top_bar_size)
            )
        )
    }
}

@Composable
fun CollectionDetailBody(
    modifier: Modifier = Modifier
) {
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectionDetailScreenPreview() {
    VirtualLibraryTheme {
        CollectionDetailScreen()
    }
}