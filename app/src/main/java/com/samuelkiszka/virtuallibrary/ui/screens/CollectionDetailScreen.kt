package com.samuelkiszka.virtuallibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme

object CollectionDetailDestination : NavigationDestination {
    override val route = "CollectionDetail"
    override val titleRes = R.string.collection_detail_screen_name
}

@Composable
fun CollectionDetailScreen() {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.collection_detail_screen_name)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectionDetailScreenPreview() {
    VirtualLibraryTheme {
        CollectionDetailScreen()
    }
}