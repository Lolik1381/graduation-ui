package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EquipmentDto(
    var id: String = "",
    var name: String = ""
) : Parcelable