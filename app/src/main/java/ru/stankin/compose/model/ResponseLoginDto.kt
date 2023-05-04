package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseLoginDto(
    var token: String = "",
    var roles: List<String> = emptyList(),
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var isTemporaryPassword: Boolean = false
) : Parcelable