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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samuelkiszka.virtuallibrary.R
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.models.AddListItemModel
import com.samuelkiszka.virtuallibrary.ui.common.DefaultAlertDialog
import com.samuelkiszka.virtuallibrary.ui.common.MembershipDialog
import com.samuelkiszka.virtuallibrary.ui.common.Rating
import com.samuelkiszka.virtuallibrary.ui.common.VirtualLibraryTopBar
import com.samuelkiszka.virtuallibrary.ui.navigation.NavigationDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailDestination
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionListDestination
import com.samuelkiszka.virtuallibrary.ui.theme.VirtualLibraryTheme
import kotlinx.coroutines.launch

object LibraryDetailDestination : NavigationDestination {
    override val route = "LibraryDetail"
    override val titleRes = R.string.screen_library_detail
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
                                    text = stringResource(id = R.string.button_edit_book),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            onClick = {
                                viewModel.toggleDropdownMenu()
                                navController.navigate("${AddEditBookDestination.route}/${viewModel.uiState.book.getJsonUrlEncoded()}")
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(id = R.string.button_remove_book),
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
                        text = stringResource(R.string.button_save_changes),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        LibraryDetailBody(
            viewModel = viewModel,
            navigateUp = { navController.navigateUp() },
            navigateToAddCollections = {
                navController.navigate(CollectionListDestination.route)
            },
            navigateToCollection = {
                navController.navigate("${CollectionDetailDestination.route}/$it")
            },
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
    navigateToAddCollections: () -> Unit,
    navigateToCollection: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val bookCollections = viewModel.bookCollections.collectAsState()
    val otherCollections = viewModel.otherCollections.collectAsState()
    DefaultAlertDialog(
        showDialogue = viewModel.showDeleteAlert,
        onDismissRequest = viewModel::toggleDeleteAlert,
        onConfirmation = {
            viewModel.deleteBook()
            viewModel.toggleDeleteAlert()
            navigateUp()
        },
        dialogTitle = stringResource(id = R.string.alert_delete_book_title),
        dialogText = stringResource(id = R.string.alert_delete_book_text).format(viewModel.uiState.book.title),
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
    MembershipDialog(
        showAddBookDialog = viewModel.showManageCollectionsDialog,
        addMembersOption = viewModel.addCollectionOption,
        showItemsToAdd = viewModel::showCollectionsToAdd,
        toggleManageMembershipDialog = viewModel::toggleManageCollectionsDialog,
        others = otherCollections.value,
        members = bookCollections.value,
        addMember = viewModel::addBookToCollection,
        removeMember = viewModel::removeBookFromCollection,
        addText = stringResource(id = R.string.button_add_collections_membership),
        removeText = stringResource(id = R.string.button_remove_collections_membership),
        noMemberToAddText = stringResource(id = R.string.resource_add_collections_to_book_no_more_collections),
        noMemberToRemoveText = stringResource(id = R.string.resource_add_collections_to_book),
        navigateToAddMembers = navigateToAddCollections
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
        Collections(
            collections = bookCollections.value,
            navigateToCollection = navigateToCollection,
            toggleManageCollectionsDialog = viewModel::toggleManageCollectionsDialog,
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.padding_extra_little)
                )
        )
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
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.demo_book_cover),
            placeholder = painterResource(id = R.drawable.demo_book_cover),
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_around))
                .width(dimensionResource(id = R.dimen.detail_image_width))
                .height(dimensionResource(id = R.dimen.detail_image_height))
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
                    book.rating,
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
    }
}

@Composable
fun Collections(
    collections: List<AddListItemModel>,
    navigateToCollection: (Long)->Unit,
    toggleManageCollectionsDialog: ()->Unit,
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
                text = stringResource(id = R.string.resource_add_book_to_collection_hint),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_little)
                    )
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
                        onClick = { navigateToCollection(it.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_little))
                    ) {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        IconButton(
            onClick = toggleManageCollectionsDialog,
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = stringResource(R.string.button_edit),
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
                contentDescription = stringResource(R.string.button_date_picker),
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
                            text = stringResource(id = R.string.button_save)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            changeDialogDateBox()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.button_cancel))
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
            text = stringResource(id = R.string.label_days_count),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = if (duration != -1) "$duration" else stringResource(R.string.label_not_started_reading),
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
                text = stringResource(id = R.string.label_notes)
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