package ru.stankin.compose.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import ru.stankin.compose.R
import ru.stankin.compose.retrofit.Repositories

@Composable
fun PhotosPreviewComponent(
    photoStorageKeys: List<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextComponent(text = "Прикрепленные фото")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (photoStorageKeys.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.no_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(5.dp)
                )
            }

            LazyRow {
                items(items = photoStorageKeys) {
                    SubcomposeAsyncImage(
                        model = "${Repositories.BASE_URL}user/v1/file/${it}",
                        contentDescription = null,
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .padding(5.dp)
                    ) {
                        val state = painter.state

                        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                            CircularProgressIndicator()
                        } else {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        }
    }
}