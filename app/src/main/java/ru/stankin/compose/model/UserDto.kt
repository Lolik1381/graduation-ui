package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDto(
    var id: String? = null,
    var username: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null
) : Parcelable