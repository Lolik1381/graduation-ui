package ru.stankin.compose.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * @param items - Мапа параметров. Key - Уникальное значение (UUID), Value - Значение выводимое на UI
 */
@Composable
fun <T> OutlinedDropdownTextField(
    items: Map<T, String> = emptyMap(),
    maxCountVisible: Int = 5,
    enabled: Boolean = true,
    enableSearch: Boolean = false,
    visible: Boolean = true,
    label: String = "",
    defaultValue: String = "",
    onClick: (key: T, value: String) -> Unit = { _, _ -> },
    onUpdatedValues: (value: String) -> Unit = {  },
    expandedHandler: (String) -> Unit = {}
) {
    var searchValue by remember { mutableStateOf(defaultValue) }
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    val focusManager = LocalFocusManager.current

    AnimatedVisibility(visible = visible) {
        Column(
            modifier = Modifier.absolutePadding(left = 16.dp, top = 8.dp, right = 16.dp, bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = searchValue,
                onValueChange = {
                    expanded = true
                    searchValue = it
                    expandedHandler(searchValue)
                },
                enabled = enableSearch,
                modifier = Modifier
                    .onGloballyPositioned { textFieldSize = it.size.toSize() }
                    .clickable(enabled = enabled) {
                        val isExpanded = !expanded
                        if (isExpanded) {
                            expandedHandler(searchValue)
                        }

                        expanded = isExpanded
                    }
                    .fillMaxWidth(),
                label = { Text(text = label) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable(enabled = enabled) {
                            val isExpanded = !expanded
                            if (isExpanded) {
                                expandedHandler(searchValue)
                            }

                            expanded = isExpanded
                        },
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            )

            AnimatedVisibility(visible = expanded) {
                val searchedItems = items
                    .filter { !enableSearch || it.value.contains(searchValue, ignoreCase = true) }

                if (searchedItems.isEmpty() || items.size / searchedItems.size < 2) {
                    onUpdatedValues(searchValue)
                }

                Card(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        searchedItems.entries.take(maxCountVisible)
                            .forEach { selectedItem ->
                                DropdownMenuItem(
                                    onClick = {
                                        onClick(selectedItem.key, selectedItem.value)
                                        searchValue = selectedItem.value
                                        expanded = false
                                        focusManager.clearFocus()
                                    }
                                ) {
                                    Text(text = selectedItem.value)
                                }
                            }
                    }
                }
            }
        }
    }
}