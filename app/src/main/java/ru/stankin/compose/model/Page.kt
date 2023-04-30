package ru.stankin.compose.model

data class Page<T>(
    var content: List<T>? = null,
    var totalPages: Int? = null,
    var totalElements: Int? = null,
    var size: Int? = null
)