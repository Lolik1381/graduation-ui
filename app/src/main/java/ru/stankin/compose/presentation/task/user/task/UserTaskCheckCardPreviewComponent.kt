package ru.stankin.compose.presentation.task.user.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.stankin.compose.R
import ru.stankin.compose.model.TaskCheckDto
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.presentation.component.DividerComponent
import ru.stankin.compose.presentation.component.PhotosPreviewComponent
import ru.stankin.compose.presentation.component.TextComponent

@Composable
fun UserTaskCheckCardPreviewComponent(
    taskTemplateCheckDto: TaskTemplateCheckDto? = null,
    taskCheckDto: TaskCheckDto? = null
) {
    
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    TextComponent(text = stringResource(id = R.string.user_task_check_name), style = MaterialTheme.typography.h6)
                    TextComponent(text = taskTemplateCheckDto?.name.orEmpty(), style = MaterialTheme.typography.h6)
                }
                Icon(
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextComponent(text = stringResource(id = R.string.user_task_check_description), style = MaterialTheme.typography.caption)
                TextComponent(text = taskTemplateCheckDto?.description.orEmpty(), style = MaterialTheme.typography.caption)
            }

            DividerComponent()

            UserTaskCheckControlValueComponent(
                taskTemplateCheck = taskTemplateCheckDto,
                controlValue = taskCheckDto?.controlValue.orEmpty(),
                enabled = false,
                onControlValueUpdate = {},
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = taskCheckDto?.comment.orEmpty(),
                    onValueChange = {},
                    label = { TextComponent(text = stringResource(id = R.string.text_field_comment_value), required = taskTemplateCheckDto?.requiredComment) },
                    placeholder = { TextComponent(text = stringResource(id = R.string.text_field_comment_value), required = taskTemplateCheckDto?.requiredComment) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    enabled = false
                )
            }
            
            PhotosPreviewComponent(photoStorageKeys = taskCheckDto?.files.orEmpty())
        }
    }
}