package ru.stankin.compose.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.stankin.compose.core.ext.requiredText

@Composable
fun TextComponent(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    required: Boolean? = false,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null
) {

    Text(
        text = text.requiredText(required),
        style = style,
        color = color,
        modifier = modifier.padding(5.dp),
        textAlign = textAlign
    )
}