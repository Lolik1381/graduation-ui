package ru.stankin.compose.viewmodel.base

interface EventHandler<T> {
    fun obtainEvent(event: T)
}