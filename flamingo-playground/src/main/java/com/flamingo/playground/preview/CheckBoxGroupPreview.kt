package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.checkboxgroup.CheckBoxData
import com.flamingo.components.checkboxgroup.CheckBoxGroup

@Preview
@Composable
fun CheckBoxGroupPreview() {
    var checkbox1 by remember { mutableStateOf(false) }
    var checkbox2 by remember { mutableStateOf(false) }
    CheckBoxGroup(
        checkBoxes = listOf(
            CheckBoxData(label = "checkBox label", checked = checkbox1, onCheckedChange = {
                checkbox1 = it
            }),
            CheckBoxData(
                label = "checkBox label ".repeat(6),
                checked = checkbox2,
                onCheckedChange = {
                    checkbox2 = it
                }),
            CheckBoxData(label = "checkBox label", disabled = true),
        ),
        label = "label",
        required = true,
        description = "description"
    )
}
