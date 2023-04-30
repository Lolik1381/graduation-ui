package ru.stankin.compose.core.ext

fun String.requiredText(required: Boolean? = true): String {
    return if (required == true) {
        "$this *"
    } else {
        this
    }
}