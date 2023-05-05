package ru.stankin.compose.presentation.task.user.tasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.core.util.Colors
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.replacePathVariable
import ru.stankin.compose.model.TaskMetadataDto
import ru.stankin.compose.presentation.barcode.QrCodeScannerScreen
import ru.stankin.compose.presentation.component.EmptyScreen
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.presentation.component.TextComponent
import ru.stankin.compose.viewmodel.UserTaskListViewModel
import ru.stankin.compose.viewmodel.event.UserTaskListEvent
import ru.stankin.compose.viewmodel.state.UserTaskListState
import ru.stankin.compose.viewmodel.viewstate.UserTaskListViewState

@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun UserTaskListComponent(
    navController: NavController = rememberNavController(),
    viewModel: UserTaskListViewModel = viewModel()
) {
    when (val state = viewModel.state) {
        is UserTaskListState.Loading -> LoadingScreen()
        is UserTaskListState.Loaded -> UserTaskListScreen(navController, viewModel, viewModel.viewState as UserTaskListViewState.Initialized)
        is UserTaskListState.Error -> ErrorScreen(onRefresh = { viewModel.obtainEvent(UserTaskListEvent.Reload) }, message = state.message)
        is UserTaskListState.Empty -> EmptyScreen(onRefresh = { viewModel.obtainEvent(UserTaskListEvent.Reload) }, message = state.message)
        is UserTaskListState.QrScanner -> QrCodeScannerScreen(callback = { viewModel.obtainEvent(UserTaskListEvent.EquipmentIdChange(it)) })
    }

    LaunchedEffect(key1 = Unit, block = { viewModel.obtainEvent(UserTaskListEvent.LoadingComplete) })
}

@Composable
fun UserTaskListScreen(navController: NavController, viewModel: UserTaskListViewModel, currentViewState: UserTaskListViewState.Initialized) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            UserTaskListSearchComponent(
                value = currentViewState.searchString,
                onChangeValue = { viewModel.obtainEvent(UserTaskListEvent.SearchStringChange(it)) },
                onClick = { viewModel.obtainEvent(UserTaskListEvent.SearchClick) },
                qrCodeScannerClick = { viewModel.obtainEvent(UserTaskListEvent.QrScannerClick) }
            )
        }

        items(items = currentViewState.tasksMetadata) {
            UserTaskListCardComponent(taskMetadata = it, onClick = { navController.navigate(Route.TASK.replacePathVariable(it.id)) })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserTaskListSearchComponent(
    value: String,
    onChangeValue: (String) -> Unit,
    onClick: () -> Unit,
    qrCodeScannerClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(0.8f)
            .height(40.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(Colors.Gray),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(5.dp),
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color.Black
        )

        BasicTextField(
            value = value,
            onValueChange = onChangeValue,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(5.dp),
            interactionSource = interactionSource,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onClick.invoke()
                }
            )
        ) { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = { Text(text = "Поиск") },
                singleLine = true,
                enabled = true,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp)
            )
        }

        IconButton(onClick = qrCodeScannerClick) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = Icons.Filled.QrCodeScanner,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun UserTaskListCardComponent(
    taskMetadata: TaskMetadataDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick.invoke() },
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                TextComponent(text = taskMetadata.header.orEmpty(), style = MaterialTheme.typography.h6)
                TextComponent(text = taskMetadata.description.orEmpty(), style = MaterialTheme.typography.caption)

                TextComponent(
                    text = "Статус: ${taskMetadata.status?.uiDescription.orEmpty()}",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}