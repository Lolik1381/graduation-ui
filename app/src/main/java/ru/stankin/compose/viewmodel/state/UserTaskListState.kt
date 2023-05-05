package ru.stankin.compose.viewmodel.state

sealed class UserTaskListState {

    /**
     * Загрузка view
     */
    object Loading: UserTaskListState()

    /**
     * View загружена
     */
    object Loaded: UserTaskListState()

    /**
     * View пустой
     */
    data class Empty(val message: String): UserTaskListState()

    /**
     * Ошибка
     */
    data class Error(val message: String) : UserTaskListState()

    /**
     * Qr scanner
     */
    object QrScanner: UserTaskListState()
}