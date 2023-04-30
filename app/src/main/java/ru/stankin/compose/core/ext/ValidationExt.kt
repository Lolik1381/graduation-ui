package ru.stankin.compose.core.ext

import ru.stankin.compose.core.exceprion.ValidationException
import java.time.ZonedDateTime

fun <T> List<T>?.checkRequiredError(required: Boolean? = null): Boolean {
    return required == true && this.isNullOrEmpty()
}

fun String?.checkRequiredError(required: Boolean? = null): Boolean {
    return required == true && this.isNullOrBlank()
}

fun String?.requiredValue(fieldName: String) {
    if (this.isNullOrBlank()) {
        throw ValidationException("Значение поля '$fieldName' обязательно!")
    }
}

fun ZonedDateTime?.isAfterCurrentDate(fieldName: String) {
    if (this?.isAfter(ZonedDateTime.now()) == false) {
        throw ValidationException("Значение поля '$fieldName' не может быть в прошлом!")
    }
}

fun List<String?>.anyOfRequiredValue(fieldName: String) {
    if (this.all { it.isNullOrBlank() }) {
        throw ValidationException("Значение поля '$fieldName' обязательно!")
    }
}