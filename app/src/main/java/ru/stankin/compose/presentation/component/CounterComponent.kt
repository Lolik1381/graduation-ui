package ru.stankin.compose.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun CounterComponent(
    modifier: Modifier = Modifier,
    current: Int = 0,
    total: Int = 0
) {

    Card(
        modifier = modifier.height(35.dp)
            .padding(5.dp)
            .shadow(2.dp, RoundedCornerShape(corner = CornerSize(8.dp))),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        TextComponent(
            text = "${current}/$total",
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}