package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupDto(
    var id: String? = null,
    var name: String? = null
) : Parcelable