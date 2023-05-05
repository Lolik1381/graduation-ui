package ru.stankin.compose.viewmodel.event

sealed class UserTaskListEvent {

    object LoadingComplete: UserTaskListEvent()
    object Reload: UserTaskListEvent()
    data class SearchStringChange(val searchText: String): UserTaskListEvent()
    data class EquipmentIdChange(val equipmentId: String): UserTaskListEvent()
    object SearchClick: UserTaskListEvent()
    object QrScannerClick: UserTaskListEvent()
}