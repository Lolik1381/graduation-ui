package ru.stankin.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.repository.AdminTaskTemplateRepository
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.event.TaskTemplateListEvent
import ru.stankin.compose.viewmodel.state.TaskTemplateListState
import ru.stankin.compose.viewmodel.viewstate.TaskTemplateListViewState

class TaskTemplateListViewModel(
    private val adminTaskTemplateRepository: AdminTaskTemplateRepository = AdminTaskTemplateRepository()
) : ViewModel(), EventHandler<TaskTemplateListEvent> {

    private val _state = mutableStateOf<TaskTemplateListState>(TaskTemplateListState.Loading)
    private val _viewState = mutableStateOf<TaskTemplateListViewState>(TaskTemplateListViewState.Empty)
    val state by _state
    val viewState by _viewState

    override fun obtainEvent(event: TaskTemplateListEvent) {
        when (event) {
            is TaskTemplateListEvent.LoadingComplete -> loading()
            is TaskTemplateListEvent.Reload -> obtainEvent(TaskTemplateListEvent.LoadingComplete)
            is TaskTemplateListEvent.TaskTemplateListCardClick -> process(TaskTemplateListState.NavigateToTaskTemplateCardList(event.taskTemplateId))
            is TaskTemplateListEvent.Completed -> process(TaskTemplateListState.Completed)
        }
    }

    private fun loading() {
        viewModelScope.launch {
            adminTaskTemplateRepository.findAll()
                .catch { _state.value = TaskTemplateListState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess {
                            val taskTemplatesDto = response.content()?.content.orEmpty()

                            if (taskTemplatesDto.isNotEmpty()) {
                                _viewState.value = TaskTemplateListViewState.Initialized(taskTemplatesDto = taskTemplatesDto)
                                _state.value = TaskTemplateListState.Loaded
                            } else {
                                _state.value = TaskTemplateListState.Empty(message = "Шаблоны контрольных списков не найдены")
                            }
                        }
                        .onFailure { _state.value = TaskTemplateListState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }

    private fun process(state: TaskTemplateListState) {
        _state.value = state
    }
}