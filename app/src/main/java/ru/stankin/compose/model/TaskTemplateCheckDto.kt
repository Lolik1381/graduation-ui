package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskTemplateCheckDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var requiredPhoto: Boolean = false,
    var requiredComment: Boolean = false,
    var requiredControlValue: Boolean = false,
    var taskTemplateCheckOrder: Int? = null,
    var controlValueType: PermissionControlValueType? = null
) : Parcelable {

    @Parcelize
    enum class PermissionControlValueType(val russianName: String) : Parcelable {
        INTEGER("Целочисленный"),
        STRING("Строковой")
    }
}