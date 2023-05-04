package ru.stankin.compose.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.stankin.compose.viewmodel.state.FieldState

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderId: Int,
    fieldState: FieldState,
    readOnly: Boolean = false,
    required: Boolean = false
) {
    val text = if (required) stringResource(id = placeholderId).plus(" *") else stringResource(id = placeholderId)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        label = { Text(text = text) },
        placeholder = { Text(text = text) },
        isError = fieldState is FieldState.Error,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .width(280.dp)
            .height(63.dp)
    )

    when (fieldState) {
        is FieldState.Ok -> {}
        is FieldState.Error -> TextComponent(text = fieldState.message, color = Color.Red)
    }
}