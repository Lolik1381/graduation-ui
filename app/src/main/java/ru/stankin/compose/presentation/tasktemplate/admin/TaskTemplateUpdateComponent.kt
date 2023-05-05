//package ru.stankin.compose.presentation.tasktemplate.admin
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.OutlinedButton
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.CheckCircleOutline
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Save
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import ru.stankin.compose.R
//import ru.stankin.compose.core.ext.navigate
//import ru.stankin.compose.core.util.Route
//import ru.stankin.compose.model.TaskTemplateDto
//import ru.stankin.compose.presentation.component.ExceptionComponent
//import ru.stankin.compose.presentation.tasktemplate.admin.viewmodel.TaskTemplateCRUDViewModel
//
//@Composable
//fun TaskTemplateUpdateComponent(
//    navController: NavController = rememberNavController(),
//    taskTemplateId: String = "",
//    viewModel: TaskTemplateCRUDViewModel = viewModel()
//) {
//    val context = LocalContext.current
//    val updateAvailable = viewModel.taskTemplateStatus == TaskTemplateDto.TaskTemplateDtoStatus.DRAFT
//    val deleteButtonVisible = viewModel.taskTemplateStatus != TaskTemplateDto.TaskTemplateDtoStatus.DELETED
//
//    LaunchedEffect(key1 = taskTemplateId) {
//        viewModel.init(taskTemplateId)
//    }
//
//    if (viewModel.errorMessage.isBlank()) {
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            item {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = stringResource(id = R.string.task_template_constructor),
//                        modifier = Modifier
//                            .padding(top = 15.dp, bottom = 5.dp),
//                        fontSize = 4.em,
//                        fontWeight = FontWeight.W500
//                    )
//
//                    if (deleteButtonVisible) {
//                        Spacer(Modifier.width(8.dp))
//
//                        IconButton(
//                            onClick = { viewModel.deleteTaskTemplate(navController, context) }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Delete,
//                                contentDescription = "Удалить",
//                                tint = Color.Red
//                            )
//                        }
//                    }
//                }
//
//                OutlinedTextField(
//                    value = viewModel.taskTemplateName,
//                    onValueChange = { viewModel.taskTemplateName = it },
//                    modifier = Modifier
//                        .padding(horizontal = 15.dp, vertical = 5.dp)
//                        .fillMaxWidth(),
//                    label = { Text(text = "${stringResource(id = R.string.task_template_constructor_name)} *") },
//                    placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_name)} *") },
//                    enabled = updateAvailable
//                )
//
//                OutlinedTextField(
//                    value = viewModel.taskTemplateDescription,
//                    onValueChange = { viewModel.taskTemplateDescription = it },
//                    modifier = Modifier
//                        .padding(horizontal = 15.dp, vertical = 5.dp)
//                        .fillMaxWidth(),
//                    label = { Text(text = "${stringResource(id = R.string.task_template_constructor_description)} *") },
//                    placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_description)} *") },
//                    enabled = updateAvailable
//                )
//            }
//
//            items(items = viewModel.taskTemplateChecks) {
//                TaskTemplateCheckCardComponent(
//                    taskTemplateStateCheck = it,
//                    onDelete = { viewModel.removeTaskTemplateCheck(it) },
//                    onUp = { viewModel.orderUp(it) },
//                    onDown = { viewModel.orderDown(it) },
//                    updateAvailable = updateAvailable
//                )
//            }
//
//            item {
//                if (updateAvailable) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 25.dp),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        OutlinedButton(
//                            onClick = viewModel::addTaskTemplateCheck,
//                            modifier = Modifier
//                                .width(60.dp)
//                                .height(50.dp)
//                                .padding(vertical = 5.dp, horizontal = 5.dp),
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Add,
//                                contentDescription = "Добавить проверку",
//                                tint = Color.Black
//                            )
//                        }
//
//                        OutlinedButton(
//                            onClick = { viewModel.updateTaskTemplate(navController, context) },
//                            modifier = Modifier
//                                .width(60.dp)
//                                .height(50.dp)
//                                .padding(vertical = 5.dp, horizontal = 5.dp),
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Save,
//                                contentDescription = "Сохранить",
//                                tint = Color.Black
//                            )
//                        }
//
//                        OutlinedButton(
//                            onClick = { viewModel.activateTaskTemplate(navController, context) },
//                            modifier = Modifier
//                                .width(60.dp)
//                                .height(50.dp)
//                                .padding(vertical = 5.dp, horizontal = 5.dp),
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.CheckCircleOutline,
//                                contentDescription = "Активировать",
//                                tint = Color.Black
//                            )
//                        }
//                    }
//                }
//
//                if (viewModel.taskTemplateStatus == TaskTemplateDto.TaskTemplateDtoStatus.ACTIVE) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 25.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        OutlinedButton(
//                            onClick = { navController.navigate(Route.TASK_CREATE, viewModel.taskTemplateId) },
//                            modifier = Modifier
//                                .height(50.dp)
//                                .padding(vertical = 5.dp, horizontal = 5.dp),
//                        ) {
//                            Text(text = "Создать задание")
//                        }
//
//                        OutlinedButton(
//                            onClick = { navController.navigate(Route.TASK_SCHEDULER_CREATE, viewModel.taskTemplateId) },
//                            modifier = Modifier
//                                .height(50.dp)
//                                .padding(vertical = 5.dp, horizontal = 5.dp),
//                        ) {
//                            Text(text = "Создать планировщик задания")
//                        }
//                    }
//                }
//            }
//        }
//    } else {
//        ExceptionComponent()
//    }
//}