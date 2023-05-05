package ru.stankin.compose.viewmodel.base

interface ViewModelFactory<T> {

    fun make(): T
}