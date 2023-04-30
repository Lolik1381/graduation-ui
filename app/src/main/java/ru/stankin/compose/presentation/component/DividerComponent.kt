package ru.stankin.compose.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DividerComponent(
    modifier: Modifier = Modifier
) {
    Divider(
        startIndent = 15.dp,
        thickness = 1.dp,
        color = Color.Gray,
        modifier = modifier.padding(0.dp, 5.dp, 15.dp, 5.dp)
    )
}