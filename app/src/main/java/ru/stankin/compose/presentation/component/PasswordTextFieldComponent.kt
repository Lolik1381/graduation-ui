package ru.stankin.compose.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.stankin.compose.R
import ru.stankin.compose.model.TextFieldStateDto

@Composable
fun PasswordTextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderId: Int,
    textFieldState: TextFieldStateDto
) {
    var passwordVisibility by remember { mutableStateOf<PasswordOptions>(PasswordOptions.HidePassword) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = stringResource(id = placeholderId)) },
        label = { Text(text = stringResource(id = placeholderId)) },
        trailingIcon = {
            IconButton(
                onClick = {
                    passwordVisibility = when (passwordVisibility) {
                        is PasswordOptions.HidePassword -> PasswordOptions.VisiblePassword
                        is PasswordOptions.VisiblePassword -> PasswordOptions.HidePassword
                    }
                }
            ) {
                Icon(
                    painter = passwordVisibility.icon.invoke(),
                    contentDescription = "Visibility Icon"
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = passwordVisibility.visualTransformation,
        isError = textFieldState.isError,
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(280.dp)
            .height(63.dp)
    )

    if (textFieldState.isError) {
        textFieldState.errorMessageId?.let { TextComponent(text = stringResource(id = it), color = Color.Red) }
    }
}

sealed class PasswordOptions(
    var icon: @Composable () -> Painter,
    var visualTransformation: VisualTransformation
) {

    object VisiblePassword : PasswordOptions({ painterResource(id = R.drawable.design_ic_visibility) }, VisualTransformation.None)
    object HidePassword : PasswordOptions({ painterResource(id = R.drawable.design_ic_visibility_off) }, PasswordVisualTransformation())
}