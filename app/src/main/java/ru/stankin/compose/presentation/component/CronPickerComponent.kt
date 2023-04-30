package ru.stankin.compose.presentation.component

import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

@Preview(showBackground = true)
@Composable
fun CronPickerComponent(
    headerText: String = ""
) {
    var cronType by remember { mutableStateOf(CronPickerType.MINUTES) }
    var minutes by remember { mutableStateOf(1) }
    var hours by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "С какой периодичностью будет запускаться планировщик?",
            modifier = Modifier.padding(10.dp, 10.dp)
        )

        OutlinedDropdownTextField(
            items = CronPickerType.values().associateWith { it.name },
            label = "Тип периодичности",
            defaultValue = cronType.name,
            onClick = { type, _ ->
                cronType = type
            }
        )

        AnimatedVisibility(
            visible = cronType == CronPickerType.MINUTES
        ) {
//            val builder = KCron.builder()

            EveryCronPickerComponent(
                value = if (minutes == -1) "" else minutes.toString(),
                onValueChange = {
                    minutes = when {
                        it.isNotBlank() && it.toIntOrNull() != null && it.toInt() > 59 -> 59
                        else -> it.toIntOrNull() ?: -1
                    }
                }
            )
//
//            Text(text = builder.minutes(TimeGroups.EveryStartingAt, "0/${minutes}")
//                .expression)
        }

        AnimatedVisibility(
            visible = cronType == CronPickerType.HOURS
        ) {
            EveryCronPickerComponent(
                value = if (hours == -1) "" else hours.toString(),
                secondText = "часов",
                onValueChange = {
                    hours = when {
                        it.isNotBlank() && it.toIntOrNull() != null && it.toInt() > 59 -> 59
                        else -> it.toIntOrNull() ?: -1
                    }
                }
            )
        }

        AnimatedVisibility(
            visible = cronType == CronPickerType.DAYS
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Switch(checked = true, onCheckedChange = null)
                    Spacer(Modifier.width(8.dp))

                    Text(text = "Каждый день")
                }

                StartAtPickerComponent(
                    hoursValue = hours.toString(),
                    onHoursChange = {
                        hours = it.toIntOrNull() ?: 0
                    },
                    minutesValue = minutes.toString(),
                    onMinutesChange = {
                        minutes = it.toIntOrNull() ?: 0
                    }
                )
            }
        }
    }
}

@Composable
private fun EveryCronPickerComponent(
    firstText: String = "Каждые",
    secondText: String = "минут",
    value: String = "1",
    onValueChange: (String) -> Unit
) {
    val width = 50.dp
    val height = 51.dp

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = firstText)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .width(width)
                .height(height),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text(text = secondText)
    }
}

@Preview(showBackground = true)
@Composable
private fun StartAtPickerComponent(
    hoursValue: String = "",
    onHoursChange: (String) -> Unit = {  },
    minutesValue: String = "",
    onMinutesChange: (String) -> Unit = {  }
) {
    val width = 50.dp
    val height = 51.dp

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Начиная с:")
        OutlinedTextField(
            value = hoursValue,
            onValueChange = {
                var value = it.ifBlank { "0" }
                if (it.toLongOrNull() != null && it.toLong() !in 0..24 ) {
                    value = "24"
                }

                onHoursChange.invoke(value)
            },
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .width(width)
                .height(height),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text(text = ":")

        OutlinedTextField(
            value = minutesValue,
            onValueChange = {
                var value = it.ifBlank { "0" }
                if (it.toLongOrNull() != null && it.toLong() !in 0..59 ) {
                    value = "59"
                }

                onMinutesChange.invoke(value)
            },
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .width(width)
                .height(height),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Parcelize
enum class CronPickerType : Parcelable {
    MINUTES, HOURS, DAYS, WEEKS, MONTHS
}

//sealed class CronPickerType(
//    var type: String,
//    var firstText: String = "Каждые",
//    var secondText: String,
//    var timeWithPicker: Boolean = false
//) {
//
//    object CronPickerMinutes : CronPickerType(type = "Поминутный", secondText = "минут")
//    object CronPickerHours : CronPickerType(type = "Почасовой", secondText = "часов")
//    object CronPickerDays : CronPickerType(type = "Подневной", secondText = "дней", timeWithPicker = true)
//    object CronPickerWeeks : CronPickerType(type = "Понедельный", secondText = "недель", timeWithPicker = true)
//    object CronPickerMonths : CronPickerType(type = "Поме́сячный", secondText = "месяцев", timeWithPicker = true)
//}