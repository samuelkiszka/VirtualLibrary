package com.samuelkiszka.virtuallibrary.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.ui.common.DefaultAlertDialog
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            VirtualLibraryTopBar(
                screenTitle = viewModel.uiState.book.title,
                canNavigateBack = true,
                navigateBack = { navController.navigateUp() },
                haveOptions = true,
                onOptionClick = viewModel::toggleDropdownMenu,
                options = {
                    DropdownMenu(
                        expanded = viewModel.dropdownMenuExpanded,
                        onDismissRequest = viewModel::toggleDropdownMenu
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            onClick = {

                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            onClick = {
                                viewModel.toggleDropdownMenu()
                                viewModel.toggleDeleteAlert()
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            if(viewModel.uiState.changed) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveChanges()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_around))
                ) {
                    Text(
                        text = stringResource(R.string.save_changes_button_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        LibraryDetailBody(
            viewModel = viewModel,
            navigateUp = { navController.navigateUp() },
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
                top = innerPadding.calculateTopPadding()
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryDetailBody(
    viewModel: LibraryDetailViewModel,
    navigateUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    DefaultAlertDialog(
        showDialogue = viewModel.showDeleteAlert,
        onDismissRequest = viewModel::toggleDeleteAlert,
        onConfirmation = {
            viewModel.deleteBook()
            viewModel.toggleDeleteAlert()
            navigateUp()
        },
        dialogTitle = stringResource(id = R.string.book_delete_title),
        dialogText = stringResource(id = R.string.book_delete_text).format(viewModel.uiState.book.title),
        icon = Icons.Filled.Delete
    )
    DateDialogBox(
        showDateDialog = viewModel.showStartDatePicker,
        changeDialogDateBox = viewModel::changeStartDatePickerVisibility,
        changedDate = viewModel.startDate,
        saveChanges = {
            viewModel.updateStartDate(viewModel.startDate.selectedDateMillis ?: 0)
        }
    )
    DateDialogBox(
        showDateDialog = viewModel.showEndDatePicker,
        changeDialogDateBox = viewModel::changeEndDatePickerVisibility,
        changedDate = viewModel.endDate,
        saveChanges = {
            viewModel.updateEndDate(viewModel.endDate.selectedDateMillis ?: 0)
        }
    )
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_around))
            .fillMaxSize()
            .verticalScroll(scrollState)
    ){
        Header(
            book = viewModel.uiState.book,
            updateRating = viewModel::updateRating
        )
        Collections()
        ReadingProgress(
            book = viewModel.uiState.book,
            duration = viewModel.duration,
            showStartDateDialog = viewModel::changeStartDatePickerVisibility,
            showEndDateDialog = viewModel::changeEndDatePickerVisibility,
            updatePagesRead = viewModel::updatePagesRead
        )
        Notes(
            viewModel.uiState.book.notes,
            updateNotes = viewModel::updateNotes,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
fun Header(
    book: BookEntity,
    updateRating: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.detail_image_height))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.coverUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            error = painterResource(id = R.drawable.demo_book_cover),
            placeholder = painterResource(id = R.drawable.demo_book_cover),
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_around))
                .width(dimensionResource(id = R.dimen.detail_image_width))
                .clip(MaterialTheme.shapes.small)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Info(
                book = book,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ){
                Rating(
                    book,
                    updateRating = updateRating
                )
            }
        }
    }
}

@Composable
fun Info(
    book: BookEntity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ){
        Text(
            text = book.author,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
        )
        Text(
            text = book.yearPublished,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
        )
        Text(
            text = "isbn",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
        )
    }
}

@Composable
fun Rating(
    bookEntity: BookEntity,
    updateRating: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Rating",
            style = MaterialTheme.typography.labelLarge
        )
        RatingBar(
            value = bookEntity.rating,
            style = RatingBarStyle.Stroke(
                strokeColor = Color.Black
            ),
            onValueChange = {
                updateRating(it)
            },
            size = 24.dp
        ) {

        }
    }
}

@Composable
fun Collections(
    collections: List<String> = listOf("Collection 1", "Collection 2"),
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_extra_little),
            )
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        if (collections.isEmpty()) {
            Text(
                text = "Add book to collection!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(collections) {
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_little))
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = stringResource(R.string.edit_button),
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ReadingProgress(
    book: BookEntity,
    duration: Int,
    showStartDateDialog: ()->Unit,
    showEndDateDialog: ()->Unit,
    updatePagesRead: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(
                dimensionResource(id = R.dimen.padding_extra_little)
            )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Slider(
                value = book.pagesRead.toFloat(),
                onValueChange = {
                    updatePagesRead(it.toInt())
                },
                valueRange = 0f..book.numberOfPages.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = Color.LightGray
                ),
                modifier = Modifier
                    .weight(1f)
            )
            BasicTextField(
                value = book.pagesRead.toString(),
                onValueChange = {
                    val num = it.toIntOrNull() ?: 0
                    updatePagesRead(if (num < book.numberOfPages) num else book.numberOfPages)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .height(30.dp)
                    .width(60.dp)
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.extraSmall,
                    )
            )
            Text(
                text = "/${book.numberOfPages}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_extra_little)
                    )
            )
        }
        Row {
            ShowDate(
                pickerName = "Start reading",
                onDateClick = showStartDateDialog,
                date = book.startDate,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_extra_little))
                    .weight(1.5f)
            )
            ShowDate(
                pickerName = "End reading",
                onDateClick = showEndDateDialog,
                date = book.endDate,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_extra_little))
                    .weight(1.5f)
            )
            ReadingDuration(
                duration = duration,
                modifier = Modifier
                    .weight(1f)
                    .padding(dimensionResource(id = R.dimen.padding_extra_little))
            )
        }
    }
}

@Composable
fun ShowDate(
    pickerName: String,
    onDateClick: ()->Unit,
    date: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                onClick = onDateClick
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .height(dimensionResource(id = R.dimen.detail_date_picker_height))
    ) {
        Text(
            text = pickerName,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (date.isNotEmpty()) {
                Text(
                    text = date,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = stringResource(R.string.date_picker),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialogBox(showDateDialog: Boolean,
                  changeDialogDateBox: ()->Unit,
                  changedDate: DatePickerState,
                  saveChanges: ()->Unit
) {
    if(showDateDialog) {
        Column {
            DatePickerDialog(
                onDismissRequest = { changeDialogDateBox() },
                confirmButton = {
                    Button(
                        onClick = {
                            saveChanges()
                            changeDialogDateBox()
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.save_button_text)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            changeDialogDateBox()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel_button_text))
                    }
                },
                content = {
                    DatePicker(
                        state = changedDate
                    )
                }
            )
        }
    }
}
@Composable
fun ReadingDuration(
    duration: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .height(dimensionResource(id = R.dimen.detail_date_picker_height))
    ) {
        Text(
            text = "Days",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = if (duration != -1) "$duration" else "Not yet!",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun Notes(
    notes: String,
    updateNotes: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = notes,
        onValueChange = {
            updateNotes(it)
        },
        label = {
            Text(
                text = "Notes"
            )
        },
        modifier = modifier
            .fillMaxSize()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryDetailScreenPreview() {
    VirtualLibraryTheme {
        LibraryDetailScreen()
    }
}