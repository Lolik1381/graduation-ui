package ru.stankin.compose.viewmodel.state

sealed class AdminProfileState {

    /**
     * Загрузка view
     */
    object Loading: AdminProfileState()

    /**
     * View загружена
     */
    object Loaded: AdminProfileState()

    /**
     * Ошибка
     */
    data class Error(val message: String): AdminProfileState()

    /**
     * Успех
     */
    object Completed: AdminProfileState()

    /**
     * Выход из системы
     */
    object NavigateToAuth: AdminProfileState()

    /**
     * Создание пользователя
     */
    object NavigateToCreateUsage: AdminProfileState()

    /**
     * Изменение пользователя
     */
    object NavigateToChangeUsage: AdminProfileState()

    /**
     * Создание группы пользователей
     */
    object NavigateToCreateGroup: AdminProfileState()

    /**
     * Изменение группы пользователей
     */
    object NavigateToChangeGroup: AdminProfileState()

    /**
     * Создание оборудования
     */
    object NavigateToCreateEquipment: AdminProfileState()

    /**
     * Изменение оборудования
     */
    object NavigateToChangeEquipment: AdminProfileState()
}