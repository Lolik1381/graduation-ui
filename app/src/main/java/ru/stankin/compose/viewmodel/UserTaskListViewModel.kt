package ru.stankin.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.repository.TaskRepository
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.event.UserTaskListEvent
import ru.stankin.compose.viewmodel.state.UserTaskListState
import ru.stankin.compose.viewmodel.viewstate.UserTaskListViewState

class UserTaskListViewModel(
    private val taskRepository: TaskRepository = TaskRepository()
) : ViewModel(), EventHandler<UserTaskListEvent> {

    private val _state = mutableStateOf<UserTaskListState>(UserTaskListState.Loading)
    private val _viewState = mutableStateOf<UserTaskListViewState>(UserTaskListViewState.Empty)
    val state by _state
    val viewState by _viewState

    override fun obtainEvent(event: UserTaskListEvent) {
        when (event) {
            is UserTaskListEvent.LoadingComplete -> loading()
            is UserTaskListEvent.Reload -> obtainEvent(UserTaskListEvent.LoadingComplete)
            is UserTaskListEvent.SearchStringChange,
            is UserTaskListEvent.EquipmentIdChange -> processingViewState(event, viewState as UserTaskListViewState.Initialized)
            is UserTaskListEvent.SearchClick -> search(viewState as UserTaskListViewState.Initialized)
            is UserTaskListEvent.QrScannerClick -> _state.value = UserTaskListState.QrScanner
        }
    }

    private fun loading() {
        viewModelScope.launch {
            taskRepository.findTasks()
                .catch { _state.value = UserTaskListState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess {
                            val taskMetadata = it.content().orEmpty()
                            if (taskMetadata.isEmpty()) {
                                _state.value = UserTaskListState.Empty("У пользователя нет назначенных контрольных проверок")
                                _viewState.value = UserTaskListViewState.Empty
                            } else {
                                _state.value = UserTaskListState.Loaded
                                _viewState.value = UserTaskListViewState.Initialized(it.content().orEmpty())
                            }
                        }
                        .onFailure { _state.value = UserTaskListState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }

    private fun processingViewState(event: UserTaskListEvent, currentViewState: UserTaskListViewState.Initialized) {
        when (event) {
            is UserTaskListEvent.SearchStringChange -> _viewState.value = currentViewState.copy(searchString = event.searchText)
            is UserTaskListEvent.EquipmentIdChange -> {
                _viewState.value = currentViewState.copy(equipmentId = event.equipmentId)
                obtainEvent(UserTaskListEvent.SearchClick)
            }
            else -> {}
        }
    }

    private fun search(currentViewState: UserTaskListViewState.Initialized) {
        viewModelScope.launch {
            taskRepository.findTasks(searchText = currentViewState.searchString, equipmentId = currentViewState.equipmentId.takeIf { it.isNotBlank() })
                .onStart { _state.value = UserTaskListState.Loading }
                .catch { _state.value = UserTaskListState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess {
                            val taskMetadata = it.content().orEmpty()
                            if (taskMetadata.isEmpty()) {
                                _state.value = UserTaskListState.Empty("У пользователя нет назначенных контрольных проверок")
                                _viewState.value = UserTaskListViewState.Empty
                            } else {
                                _state.value = UserTaskListState.Loaded
                                _viewState.value = currentViewState.copy(tasksMetadata = it.content().orEmpty())
                            }
                        }
                        .onFailure { _state.value = UserTaskListState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }
}