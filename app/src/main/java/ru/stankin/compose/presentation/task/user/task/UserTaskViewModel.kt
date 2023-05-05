package ru.stankin.compose.presentation.task.user.task

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.stankin.compose.core.ext.find
import ru.stankin.compose.core.ext.update
import ru.stankin.compose.core.util.FileUtils
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.errorMessage
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.APIs
import ru.stankin.compose.model.FileDto
import ru.stankin.compose.model.TaskCheckDto
import ru.stankin.compose.model.TaskDto
import ru.stankin.compose.model.TaskTemplateCheckDto


class UserTaskViewModel : ViewModel() {

    private val _task = mutableStateOf<TaskDto?>(null)

    private val _taskChecks = mutableStateListOf<TaskCheckState>()

    private val _isError = mutableStateOf(false)
    private val _errorMessage = mutableStateOf("")

    private val _taskRepository = APIs.taskApi
    private val _userFileRepository = APIs.userFileApi
    private val _userTaskCheckRepository = APIs.userTaskCheckApi

    @SuppressLint("StaticFieldLeak")
    private lateinit var _context: Context

    val task: TaskDto?
        get() = _task.value

    val taskTemplateChecks: List<TaskTemplateCheckDto>
        get() = _task.value?.taskTemplate?.taskTemplateChecks?.sortedBy { it.taskTemplateCheckOrder } ?: emptyList()

    val taskChecks: List<TaskCheckState>
        get() = _taskChecks

    val isError: Boolean
        get() = _isError.value

    val errorMessage: String
        get() = _errorMessage.value

    fun init(taskId: String?, context: Context) {
        _context = context

        if (taskId == null) {
            return
        }

        viewModelScope.launch {
            _taskRepository.findTaskById(taskId)
                .onSuccess { responce ->
                    val taskDto = responce.content()

                    val taskCheckStates = taskDto?.taskTemplate?.taskTemplateChecks?.sortedBy { it.taskTemplateCheckOrder }
                        ?.mapNotNull {
                            taskDto.taskChecks.find(it)
                        }?.map {
                            TaskCheckState(
                                _taskCheck = mutableStateOf(it),
                                _isEnabledCard = mutableStateOf(false)
                            )
                        }

                    taskCheckStates?.firstOrNull { it.taskCheck.status == TaskCheckDto.TaskCheckStatus.ACTIVE }?.isEnabledCard = true

                    _task.value = taskDto
                    _taskChecks.update(taskCheckStates)
                    setError()
                }
                .onFailure { setError("Произошла ошибка при получении задания") }
        }
    }

    fun findByTemplate(taskTemplateCheckDto: TaskTemplateCheckDto): TaskCheckState {
        return taskChecks.first { it.taskCheck.taskTemplateCheckId == taskTemplateCheckDto.id }
    }

    fun save(taskCheckState: TaskCheckState?) {
        if (taskCheckState == null) {
            return
        }

        viewModelScope.launch {
            taskCheckState.isLoadedCard = true

            val filesPart = taskCheckState.photoUri.map { uri ->
                val file = FileUtils.getFileFromUri(_context, uri)

                MultipartBody.Part.createFormData("files", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            }

            if (filesPart.isNotEmpty()) {
                _userFileRepository.saveFiles(filesPart)
                    .onSuccess { response ->
                        saveCheck(taskCheckState, response.content())
                    }
                    .onFailure {
                        _isError.value = true
                        _errorMessage.value = it.errorMessage()
                    }
            } else {
                saveCheck(taskCheckState, null)
            }
        }
    }

    fun getNumberCompleteChecks(): Int {
        return _task.value?.taskChecks?.count { it.status == TaskCheckDto.TaskCheckStatus.FINISHED } ?: 0
    }

    fun getTotalChecks(): Int {
        return _task.value?.taskChecks?.size ?: 0
    }

    private fun setError(errorMessage: String? = null) {
        _isError.value = !errorMessage.isNullOrBlank()
        _errorMessage.value = errorMessage.orEmpty()
    }

    private fun saveCheck(taskCheckState: TaskCheckState, photoIds: List<FileDto>?) {
        viewModelScope.launch {
            _userTaskCheckRepository.update(
                id = taskCheckState.taskCheck.id!!,
                comment = taskCheckState.commentValue.takeIf { it.isNotBlank() },
                controlValue = taskCheckState.controlValue.takeIf { it.isNotBlank() },
                photoIds = photoIds?.map { it.id!! }
            ).onSuccess {
                init(task?.id!!, _context)
            }.onFailure {
                _isError.value = true
                _errorMessage.value = it.errorMessage()
            }
        }
    }
}

class TaskCheckState(
    private val _taskCheck: MutableState<TaskCheckDto>,
    private val _isEnabledCard: MutableState<Boolean>,
    private val _isLoadedCard: MutableState<Boolean> = mutableStateOf(false)
) {

    private val _controlValue = mutableStateOf<String?>(null)
    private val _commentValue = mutableStateOf<String?>(null)
    private val _photoUri = mutableStateOf<List<Uri>>(emptyList())

    var taskCheck: TaskCheckDto
        get() = _taskCheck.value
        set(value) {
            _taskCheck.value = value
        }

    var controlValue: String
        get() = _controlValue.value.orEmpty()
        set(value) {
            _controlValue.value = value
        }

    var commentValue: String
        get() = _commentValue.value.orEmpty()
        set(value) {
            _commentValue.value = value
        }

    var photoUri: List<Uri>
        get() = _photoUri.value
        set(value) {
            _photoUri.value = value
        }

    var isEnabledCard: Boolean
        get() = _isEnabledCard.value
        set(value) { _isEnabledCard.value = value }

    var isLoadedCard: Boolean
        get() = _isLoadedCard.value
        set(value) { _isLoadedCard.value = value }
}