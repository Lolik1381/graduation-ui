package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestChangePasswordDto(
    var login: String? = null,
    var oldPassword: String? = null,
    var newPassword: String? = null
) : Parcelable