package ru.stankin.compose.viewmodel.state

sealed class ChangePasswordState {

    /**
     * Загрузка view
     */
    object Loading: ChangePasswordState()

    /**
     * View загружена
     */
    object Loaded: ChangePasswordState()

    /**
     * Попытка входа в систему
     */
    object Processing: ChangePasswordState()

    /**
     * Ошибка при попытке входа в систему
     */
    data class Error(val message: String) : ChangePasswordState()

    /**
     * Успех
     */
    object Completed: ChangePasswordState()
}