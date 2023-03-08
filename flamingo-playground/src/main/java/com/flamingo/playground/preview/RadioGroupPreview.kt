package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.radiogroup.RadioButtonData
import com.flamingo.components.radiogroup.RadioGroup

@Preview
@Composable
fun RadioGroupPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    RadioGroup(
        radioButtons = listOf(
            RadioButtonData(
                label = "radio button label",
                selected = selectedIndex == 0,
                onClick = { selectedIndex = 0 }),
            RadioButtonData(
                label = "long long long long long long long radio button label",
                selected = selectedIndex == 1,
                onClick = { selectedIndex = 1 }),
            RadioButtonData(
                label = "long long long long radio button label",
                disabled = true,
                selected = selectedIndex == 2,
                onClick = { selectedIndex = 2 }),
            RadioButtonData(
                label = "long radio button label",
                selected = selectedIndex == 3,
                onClick = { selectedIndex = 3 }),
            RadioButtonData(
                label = "long long long long long radio button label",
                selected = selectedIndex == 4,
                onClick = { selectedIndex = 4 }),
        ),
        label = "label",
        required = true,
        description = "description"
    )
}