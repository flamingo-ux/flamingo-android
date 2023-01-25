package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.switchgroup.SwitchData
import com.flamingo.components.switchgroup.SwitchGroup

@Preview
@Composable
fun SwitchGroupPreview() {
    var switch1 by remember { mutableStateOf(false) }
    var switch2 by remember { mutableStateOf(false) }
    SwitchGroup(
        switches = listOf(
            SwitchData(label = "switch label", checked = switch1, onCheckedChange = {
                switch1 = it
            }),
            SwitchData(
                label = "switch label ".repeat(6),
                checked = switch2,
                onCheckedChange = {
                    switch2 = it
                }),
            SwitchData(label = "switch label", disabled = true),
        ),
        label = "label",
        required = true,
        description = "description"
    )
}