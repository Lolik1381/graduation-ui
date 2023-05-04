package ru.stankin.compose.presentation.tasktemplate.admin.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.errorMessage
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.Repositories
import ru.stankin.compose.model.TaskTemplateDto

class TaskTemplateViewModel : ViewModel() {

    private val _taskTemplateList = mutableStateListOf<TaskTemplateDto>()
    private val _errorMessage = mutableStateOf("")

    val taskTemplateList
        get() = _taskTemplateList.toList()

    var errorMessage
        get() = _errorMessage.value
        set(value) {
            _errorMessage.value = value
        }

    fun init() {
        viewModelScope.launch {
            Repositories.adminTaskTemplateApi.findAll()
                .onSuccess {
                    _taskTemplateList.clear()
                    _taskTemplateList.addAll(it.content()?.content ?: emptyList())

                    _taskTemplateList.sortBy { taskTemplate -> taskTemplate.status?.order }
                }
                .onFailure { errorMessage = it.errorMessage() }
        }
    }
}