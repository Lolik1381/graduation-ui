package ru.stankin.compose.presentation.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.stankin.compose.presentation.component.viewmodel.UserOrGroupViewModel

@Preview
@Composable
fun UserOrGroupSelectorComponent(
    componentViewModel: UserOrGroupViewModel = viewModel(),
    onClick: (UserOrGroupState, String) -> Unit = { _, _ -> }
) {
    var targetType by remember { mutableStateOf(UserOrGroupState.NOT_SELECTED) }

    if (componentViewModel.isError) {
        Toast.makeText(LocalContext.current, componentViewModel.errorMessage, Toast.LENGTH_SHORT).show()
    }

    Column {
        OutlinedDropdownTextField(
            items = mapOf(
                UserOrGroupState.USER to UserOrGroupState.USER.uiDescription,
                UserOrGroupState.GROUP to UserOrGroupState.GROUP.uiDescription
            ),
            enableSearch = false,
            label = "Тип привязки",
            onClick = { state, _ ->
                targetType = state
                componentViewModel.search(targetType)
            }
        )

        OutlinedDropdownTextField(
            items = componentViewModel.getSelectedTargetInfo(targetType),
            enableSearch = true,
            label = "Пользователь или группа",
            onClick = { id, _ -> onClick.invoke(targetType, id) },
            onUpdatedValues = { componentViewModel.search(targetType, it) }
        )
    }
}

enum class UserOrGroupState(
    val uiDescription: String
) {

    USER("Пользователь"),
    GROUP("Группа"),
    NOT_SELECTED("")
}