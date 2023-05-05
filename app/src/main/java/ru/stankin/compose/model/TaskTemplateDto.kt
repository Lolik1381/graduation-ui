package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskTemplateDto(
    var id: String = "",
    var header: String = "",
    var description: String = "",
    var status: TaskTemplateDtoStatus = TaskTemplateDtoStatus.DRAFT,
    var equipment: EquipmentDto = EquipmentDto(),
    var taskTemplateChecks: MutableList<TaskTemplateCheckDto> = mutableListOf()
) : Parcelable {

    enum class TaskTemplateDtoStatus(
        var order: Int,
        var description: String
    ) {
        DRAFT(1, "Черновик"), ACTIVE(0, "Активирован"), DELETED(2, "Удален")
    }
}