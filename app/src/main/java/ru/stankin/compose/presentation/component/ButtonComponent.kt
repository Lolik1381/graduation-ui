package ru.stankin.compose.presentation.component

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource

@Composable
fun ButtonComponent(
    textId: Int,
    onClick: () -> Unit
) {
    
    Button(
        onClick = onClick
    ) {
        Text(text = stringResource(id = textId))
    }
}