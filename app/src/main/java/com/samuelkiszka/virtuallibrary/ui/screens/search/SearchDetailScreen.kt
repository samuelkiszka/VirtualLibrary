package com.samuelkiszka.virtuallibrary.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryBottomBar
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object SearchDetailDestination : NavigationDestination {
    override val route = "SearchDetail"
    override val titleRes = R.string.search_detail_screen_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDetailScreen(
    navController: NavHostController = NavHostController(LocalContext.current)
) {
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitleId = R.string.search_detail_screen_name,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() }
            )
        },
        bottomBar = {
            SaveButton()
        }
    ) { innerPadding ->
        SearchDetailBody(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = innerPadding.calculateTopPadding()
            )
        )
    }
}

@Composable
fun SearchDetailBody(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
            .fillMaxSize()
    ) {
        Header()
        ValueEditingFields()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("")
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            error = painterResource(id = R.drawable.demo_book_cover),
            placeholder = painterResource(id = R.drawable.demo_book_cover),
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_around))
                .width(150.dp)
        )
        Column(
            modifier = Modifier
        ) {
            Text(
                text = "Harry Potter and the Philosopher's Stone",
            )
            Text(
                text = "J.K. Rowling",
            )
        }
    }
}

@Composable
fun ValueEditingFields() {
    Column() {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Author") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Year published") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Number of pages") },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Save")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchDetailScreenPreview() {
    VirtualLibraryTheme {
        SearchDetailScreen()
    }
}