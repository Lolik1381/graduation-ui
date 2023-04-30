package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskTemplateDto(
    var id: String? = null,
    var header: String? = null,
    var description: String? = null,
    var status: TaskTemplateDtoStatus? = null,
    var taskTemplateChecks: MutableList<TaskTemplateCheckDto>? = null
) : Parcelable {

    enum class TaskTemplateDtoStatus(
        var order: Int,
        var description: String
    ) {
        DRAFT(1, "Черновик"), ACTIVE(0, "Активирован"), DELETED(2, "Удален")
    }
}