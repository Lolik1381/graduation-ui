package ru.stankin.compose.model

data class RequestLoginDto(
    var login: String? = null,
    var password: String? = null
)