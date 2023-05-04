package ru.stankin.compose.viewmodel.state

sealed class AuthState {

    /**
     * Загрузка view
     */
    object Loading: AuthState()

    /**
     * View загружена
     */
    object Loaded: AuthState()

    /**
     * Попытка входа в систему
     */
    object Processing: AuthState()

    /**
     * Обнаружен временный пароль
     */
    object TemporaryPassword: AuthState()

    /**
     * Ошибка при попытке входа в систему
     */
    data class Error(val message: String) : AuthState()

    /**
     * Успех
     */
    object Completed: AuthState()
}