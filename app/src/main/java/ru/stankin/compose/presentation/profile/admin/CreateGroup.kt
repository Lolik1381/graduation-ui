package ru.stankin.compose.presentation.profile.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.stankin.compose.R
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.presentation.component.TextFieldComponent
import ru.stankin.compose.viewmodel.CreateGroupViewModel
import ru.stankin.compose.viewmodel.base.FieldState
import ru.stankin.compose.viewmodel.event.CreateGroupEvent
import ru.stankin.compose.viewmodel.state.CreateGroupState

@Composable
fun CreateGroup(
    navController: NavController,
    viewModel: CreateGroupViewModel
) {

    when (val state = viewModel.state) {
        is CreateGroupState.Loading -> LoadingScreen()
//        is CreateGroupState.Loaded -> CreateGroupScreen(viewModel = viewModel, viewState = viewModel.viewState as CreateGroupViewState.Initialized)
        is CreateGroupState.Error -> ErrorScreen(onRefresh = { viewModel.obtainEvent(CreateGroupEvent.Reload) }, message = state.message)
    }

    LaunchedEffect(key1 = Unit, block = { viewModel.obtainEvent(CreateGroupEvent.LoadingComplete) })
}

@Composable
fun CreateGroupScreen(
//    viewModel: CreateGroupViewModel,
//    viewState: CreateGroupViewState.Initialized
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.construct_groups),
            modifier = Modifier.padding(5.dp)
        )

//        TextFieldComponent(
//            value = viewState.name,
//            onValueChange = { viewModel.obtainEvent(CreateGroupEvent.ChangeGroupName(it)) },
//            placeholderId = R.string.group_name,
//            fieldState = viewState.nameValidatedError
//        )
        TextFieldComponent(
            value = "",
            onValueChange = {  },
            placeholderId = R.string.group_name,
            fieldState = FieldState.Ok
        )

        Card(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(0.8f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(0.2f),
                    onClick = {  }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    text = "Добавить пользователей"
                )
            }
        }
    }
}
//
//@Composable
//fun AddUsers() {
//    AlertDialog(
//        onDismissRequest = {  },
//        modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 16.dp),
//        text = {
//            Column {
//                LazyColumn(
//                    modifier = Modifier.weight(1f), contentPadding = PaddingValues(0.dp, 8.dp)
//                ) {
//                    items(viewModel.checkableList) { level ->
//                        MultiCheckableItem(level, viewModel.checkableState)
//                        if (level.nestedItems.isNotEmpty()) {
//                            GetItems(level.nestedItems, viewModel.checkableState)
//                        }
//                    }
//                }
//
//                Row(
//                    modifier = Modifier
//                        .padding(0.dp, 8.dp, 0.dp, 0.dp)
//                        .align(Alignment.End)
//                ) {
//                    Button(
//                        onClick = {
//                            viewModel.openDialog.value = false
//                            updateCheckableList(viewModel.checkableList, viewModel.checkableState)
//                        }, modifier = Modifier
//                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
//                    ) {
//                        Text("Set")
//                    }
//
//                    Button(
//                        onClick = {
//                            viewModel.openDialog.value = false
//                            viewModel.checkableState.clear()
//                            viewModel.checkableState.putAll(getCheckableListStates(viewModel.checkableList))
//                        },
//                        modifier = Modifier
//                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
//                    ) {
//                        Text("Cancel")
//                    }
//                }
//            }
//        },
//        confirmButton = { }
//    )
//}