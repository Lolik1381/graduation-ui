package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ControlValueTemplateDto(
    var name: String? = null,
    var type: PermissionControlValueType? = null

) : Parcelable {

    enum class PermissionControlValueType {
        INTEGER, STRING, BOOLEAN
    }
}