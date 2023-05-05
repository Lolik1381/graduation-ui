package ru.stankin.compose.viewmodel.state

sealed class TaskTemplateState {

    /**
     * Загрузка view
     */
    object Loading: TaskTemplateState()

    /**
     * View загружена
     */
    object Loaded: TaskTemplateState()

    /**
     * Сохоанение
     */
    object SavedProcess: TaskTemplateState()

    /**
     * Успех
     */
    object Success: TaskTemplateState()

    /**
     * Ошибка
     */
    data class Error(val message: String): TaskTemplateState()

    /**
     * Окончание работы в view
     */
    object Completed: TaskTemplateState()

    /**
     * Переход на страницу создания контрольного списка
     */
    data class NavigateToTaskCreate(val taskTemplateId: String): TaskTemplateState()

    /**
     * Переход на страницу создания планировщика
     */
    data class NavigateToTaskSchedulerCreate(val taskTemplateId: String): TaskTemplateState()
}