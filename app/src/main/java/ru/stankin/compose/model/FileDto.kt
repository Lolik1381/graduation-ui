package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileDto(
    var id: String? = null,
    var name: String? = null,
    var extension: String? = null
) : Parcelable