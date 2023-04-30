package ru.stankin.compose.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DateTimePickerComponent(
    dateTimePickerDialogState: MaterialDialogState = rememberMaterialDialogState(),
    positiveOnClick: (date: LocalDate, time: LocalTime) -> Unit,
    negativeOnClick: () -> Unit = {  }
) {
    var date by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var time by rememberSaveable { mutableStateOf(LocalTime.now()) }

    MaterialDialog(
        dialogState = dateTimePickerDialogState,
        buttons = {
            positiveButton(
                text = "Ок",
                onClick = { positiveOnClick(date, time) }
            )
            negativeButton(
                text = "Назад",
                onClick = negativeOnClick
            )
        }
    ) {
        datepicker(
            title = "Дата окончания"
        ) {
            date = it
        }

        timepicker(
            title = "Время окончания"
        ) {
            time = it
        }
    }
}