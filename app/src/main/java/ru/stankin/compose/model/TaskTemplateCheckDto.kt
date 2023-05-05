package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskTemplateCheckDto(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var requiredPhoto: Boolean = false,
    var requiredComment: Boolean = false,
    var requiredControlValue: Boolean = false,
    var taskTemplateCheckOrder: Int = 0,
    var controlValueType: PermissionControlValueType = PermissionControlValueType.STRING
) : Parcelable {

    @Parcelize
    enum class PermissionControlValueType(val russianName: String) : Parcelable {
        INTEGER("Целочисленный"),
        STRING("Строковой")
    }
}