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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.stankin.compose.R
import ru.stankin.compose.core.ext.checkRequiredError
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.presentation.component.DividerComponent
import ru.stankin.compose.presentation.component.PhotoPickerComponent
import ru.stankin.compose.presentation.component.TextComponent

@Composable
fun UserTaskCheckCardComponent(
    taskTemplateCheckDto: TaskTemplateCheckDto,
    viewModel: UserTaskViewModel
) {
    var photoRequiredError by remember { mutableStateOf(false) }
    var commentRequiredError by remember { mutableStateOf(false) }
    var controlValueRequiredError by remember { mutableStateOf(false) }

    val componentState = viewModel.findByTemplate(taskTemplateCheckDto)

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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextComponent(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    text = taskTemplateCheckDto.name.orEmpty(),
                    style = MaterialTheme.typography.h6
                )

                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            TextComponent(text = taskTemplateCheckDto.description.orEmpty(), style = MaterialTheme.typography.caption)
            DividerComponent()

            UserTaskCheckControlValueComponent(
                taskTemplateCheck = taskTemplateCheckDto,
                isError = controlValueRequiredError,
                controlValue = componentState.controlValue,
                enabled = componentState.isEnabledCard,
                onControlValueUpdate = {
                    componentState.controlValue = it
                }
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
                    value = componentState.commentValue,
                    onValueChange = {
                        componentState.commentValue = it
                    },
                    label = {
                        TextComponent(text = stringResource(id = R.string.text_field_comment_value), required = taskTemplateCheckDto.requiredComment)
                    },
                    placeholder = {
                        TextComponent(text = stringResource(id = R.string.text_field_comment_value), required = taskTemplateCheckDto.requiredComment)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    isError = commentRequiredError,
                    enabled = componentState.isEnabledCard,
                )
            }

            PhotoPickerComponent(
                required = taskTemplateCheckDto.requiredPhoto,
                isError = photoRequiredError,
                enabled = componentState.isEnabledCard,
                selectedImageUris = componentState.photoUri,
                onResultPicker = {
                    componentState.photoUri = it
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = {
                        photoRequiredError = componentState.photoUri.checkRequiredError(taskTemplateCheckDto.requiredPhoto)
                        controlValueRequiredError = componentState.controlValue.checkRequiredError(taskTemplateCheckDto.requiredControlValue)
                        commentRequiredError = componentState.commentValue.checkRequiredError(taskTemplateCheckDto.requiredComment)

                        if (!controlValueRequiredError && !commentRequiredError && !photoRequiredError) {
                            viewModel.save(componentState)
                        }
                    },
                    enabled = componentState.isEnabledCard
                ) {
                    if (componentState.isLoadedCard) {
                        CircularProgressIndicator()
                    } else {
                        TextComponent(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    }
}