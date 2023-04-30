package ru.stankin.compose.presentation.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.stankin.compose.R

@Preview
@Composable
fun PhotoPickerComponent(
    required: Boolean? = true,
    isError: Boolean = true,
    enabled: Boolean = true,
    selectedImageUris: List<Uri> = emptyList(),
    onResultPicker: (List<Uri>) -> Unit = {}
) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = onResultPicker
    )

    val errorColor = if (isError) MaterialTheme.colors.error else Color.Unspecified

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextComponent(text = "Прикрепить фото", required = required, color = errorColor)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                enabled = enabled,
                modifier = Modifier.padding(5.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }

            if (selectedImageUris.isEmpty()) {
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
                items(selectedImageUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .padding(5.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}