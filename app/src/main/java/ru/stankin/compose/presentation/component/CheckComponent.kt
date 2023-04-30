package ru.stankin.compose.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CheckComponent(
    placeholder: String,
    value: String,
    onChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(text = value) },
        label = { Text(text = value) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//        visualTransformation = passwordVisibility.visualTransformation,
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(280.dp)
            .height(63.dp)
    )
//
//    if (textFieldState.isError) {
//        textFieldState.errorMessageId?.let { ErrorTextComponent(textId = it) }
//    }
}