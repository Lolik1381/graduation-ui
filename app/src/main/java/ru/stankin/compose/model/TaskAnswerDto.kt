package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskAnswerDto(

    var id: String? = null,
    var taskId: String? = null,
    var taskVerificationId: String? = null,
    var photoStorageKey: String? = null,
    var comment: String? = null,
    var controlValue: String? = null
) : Parcelable