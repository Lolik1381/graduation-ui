package ru.stankin.compose.core.ext

import ru.stankin.compose.model.Page

fun <T> MutableCollection<T>.update(values: Collection<T>?): MutableCollection<T> {
    this.clear()
    values?.let { this.addAll(it) }
    return this
}

fun <T> MutableCollection<T>.update(values: Page<T>?): MutableCollection<T> {
    this.clear()
    values?.let { this.addAll(it.content ?: emptyList()) }
    return this
}