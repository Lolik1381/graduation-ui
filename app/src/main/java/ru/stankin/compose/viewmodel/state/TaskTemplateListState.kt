package ru.stankin.compose.viewmodel.state

sealed class TaskTemplateListState {

    /**
     * Загрузка view
     */
    object Loading: TaskTemplateListState()

    /**
     * View загружена
     */
    object Loaded: TaskTemplateListState()

    /**
     * Страница пустая
     */
    data class Empty(val message: String): TaskTemplateListState()

    /**
     * Ошибка
     */
    data class Error(val message: String): TaskTemplateListState()

    /**
     * Успех
     */
    object Completed: TaskTemplateListState()

    /**
     * Навигация к карте шаблона контрольной проверки
     */
    data class NavigateToTaskTemplateCardList(val taskTemplateId: String): TaskTemplateListState()
}