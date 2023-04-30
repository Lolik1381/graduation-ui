package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextFieldStateDto(
    var isError: Boolean = false,
    var errorMessageId: Int? = null
) : Parcelable