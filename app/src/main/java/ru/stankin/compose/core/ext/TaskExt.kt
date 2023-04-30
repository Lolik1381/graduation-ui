package ru.stankin.compose.core.ext

import ru.stankin.compose.model.TaskCheckDto
import ru.stankin.compose.model.TaskTemplateCheckDto

fun List<TaskCheckDto>?.find(template: TaskTemplateCheckDto): TaskCheckDto? {
    return this?.find { it.taskTemplateCheckId == template.id }
}