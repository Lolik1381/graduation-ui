package ru.stankin.compose.presentation.component.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.stankin.compose.core.ext.update
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.datasource.Repositories
import ru.stankin.compose.model.GroupDto
import ru.stankin.compose.model.UserDto
import ru.stankin.compose.presentation.component.UserOrGroupState

class UserOrGroupViewModel : ViewModel() {

    private val _users = mutableStateListOf<UserDto>()
    private val _groups = mutableStateListOf<GroupDto>()
    private val _isError = mutableStateOf(false)
    private val _errorMessage = mutableStateOf<String?>(null)

    private val _adminUserRepository = Repositories.adminUserRepository

    val isError: Boolean
        get() = _isError.value

    val errorMessage: String?
        get() = _errorMessage.value

    fun search(userOrGroupState: UserOrGroupState, searchValue: String? = null) {
        viewModelScope.launch {
            when (userOrGroupState) {
                UserOrGroupState.USER -> _adminUserRepository.findAll(searchText = searchValue, size = 5)
                    .onSuccess { _users.update(it.content()) }
                    .onFailure {
                        _isError.value = true
                        _errorMessage.value = "Ошибка при получении пользователей"
                    }
                UserOrGroupState.GROUP -> _adminUserRepository.findAllGroups(searchName = searchValue, size = 5)
                    .onSuccess { _groups.update(it.content()) }
                    .onFailure {
                        _isError.value = true
                        _errorMessage.value = "Ошибка при получении групп пользователей"
                    }
                UserOrGroupState.NOT_SELECTED -> Unit
            }
        }
    }

    fun getSelectedTargetInfo(userOrGroupState: UserOrGroupState): Map<String, String> {
        return when (userOrGroupState) {
            UserOrGroupState.USER -> _users.associate { checkNotNull(it.id) to "${it.firstName} ${it.lastName}" }
            UserOrGroupState.GROUP -> _groups.associate { checkNotNull(it.id) to checkNotNull(it.name) }
            UserOrGroupState.NOT_SELECTED -> mapOf()
        }
    }
}