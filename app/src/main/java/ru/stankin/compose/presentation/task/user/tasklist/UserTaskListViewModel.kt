package ru.stankin.compose.presentation.task.user.tasklist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.stankin.compose.core.ext.update
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.Repositories
import ru.stankin.compose.model.TaskMetadataDto

class UserTaskListViewModel : ViewModel() {

    private val _taskMetadata = mutableStateListOf<TaskMetadataDto>()

    private val _isError = mutableStateOf(false)
    private val _errorMessage = mutableStateOf("")

    val tasksInfo: List<TaskMetadataDto>
        get() = _taskMetadata

    val isError: Boolean
        get() = _isError.value

    val errorMessage: String
        get() = _errorMessage.value

    fun init() {
        viewModelScope.launch {
            Repositories.taskApi.findTasks()
                .onSuccess { _taskMetadata.update(it.content()) }
                .onFailure { _errorMessage.value = "Невозможно получить задания" }
        }
    }
}